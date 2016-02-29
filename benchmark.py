import concolic
from sys import argv, exit
from random import seed, randint
from collections import namedtuple
from time import time
from shutil import copy
from os import chdir
from subprocess import check_output

#INPUT_FILE="catg_tmp/inputs"
CMD_RESTART_COUNTING_SERVER="../PathConditionsProbability/PathConditionsProbability/restartServer"
STRATEGY_CONFIG_FILES={"RANDOM":"catg.conf.random","DFS":"catg.conf.dfs","QUANTOLIC":"catg.conf.quantolic","MCTS":"catg.conf.mcts-quantolic"}
CONFIG_FILE="catg.conf"
CATG_OUTPUT_FILE="catg_tmp/catg_output"
CSV_OUTPUT_DIR="results"

TCAS=("tests.casestudies.tcas.Driver",[{'type':'bool'} for i in range(3)] + [{'type':'int','lo':-10000,'hi':10000} for i in range(9)])
SIENA=("tests.casestudies.sir.siena.DecoderTest",[{'type':'int','lo':-128,'hi':127} for i in range(5)])
JTOPAS=("tests.casestudies.sir.jtopasV0.TestPluginTokenizer",[{'type':'int','lo':-128,'hi':127} for i in range(20)])
BIN_TREE=("tests.casestudies.sir.binarytree.BinarySearchTreeTest",[{'type':'int','lo':-100,'hi':100} for i in range(5)])

Arguments = namedtuple('Arguments',['maxIterations','className','offline','verbose','D', 'arguments'])

class Enum(set):
    def __getattr__(self, name):
        if name in self:
            return name
        raise AttributeError

Strategies=Enum(STRATEGY_CONFIG_FILES.keys())


def gen_input_file(inputvars):
    data = []
    var_input = None
    for var in inputvars:
        if var['type'] == 'int':
            hi = var['hi']
            lo = var['lo']
            var_input = randint(lo,hi)
        elif var['type'] == 'bool':
            var_input = randint(0,1)
        data.append(var_input)
 #   with open(INPUT_FILE,'w') as input_file:
        text = '\n'.join([str(x) for x in data])
 #       input_file.write(content)
    return (data,text)


def run_janala_once(classname,first_input):
    args = Arguments(maxIterations=1,className=classname,offline=False,verbose=False,D=None,arguments="")
    concolic.handle_args(args)
    concolic.concolic(first_input)
    chdir("..")

    # output from subjects may result in grep thinking that the log is a binary file;
    #-a will force it to handle the input as a text file
    cov_string = check_output("grep -a -F '[quantolic] domain coverage for this path:' '{}' | cut -f 7 -d ' ' ".format(CATG_OUTPUT_FILE),shell=True)
    cov = float(cov_string)
    path = check_output("grep -a -F '[quantolic] full path: ' {} | cut -f 4-  -d ' '".format(CATG_OUTPUT_FILE),shell=True)
    return (path,cov)


def random_janala(classname,var_data,ntimes,start):
    distinct_path_inputs = []
    distinct_paths = []

    path_set = set()
    repeated_paths = 0

    coverage = []
    cov_timestamps = []
    
    cumulative_coverage = 0
#    cumulative_coverage_track = []

    for i in range(ntimes):
        input_data,text_input = gen_input_file(var_data)
        path,cov = run_janala_once(classname,text_input)

        if path in path_set:
            repeated_paths += 1
        else:
            path_set.add(path)
            distinct_path_inputs.append(input_data)
            distinct_paths.append(path)
            coverage.append(cov)
            cov_timestamps.append(time() - start)
            cumulative_coverage += cov
            print "[benchmark:random] cumulative coverage: {}".format(cumulative_coverage)
            if cumulative_coverage >= 1:
                break
#        cumulative_coverage_track.append(cumulative_coverage)

    return {"coverage":coverage,"timestamps":cov_timestamps,"repeated_paths":repeated_paths,"inputs":distinct_path_inputs}


def heuristic_janala(classname,ntimes,start):
    #run janala, process output
    args = Arguments(maxIterations=ntimes,className=classname,offline=False,verbose=False,D=None,arguments="")
    concolic.handle_args(args)
    concolic.concolic()
    chdir("..")
    cov_string = check_output("grep -a -F '[quantolic] domain coverage for this path:' '{}' | cut -f 7,9 -d ' ' ".format(CATG_OUTPUT_FILE),shell=True)
    cov_string_split = cov_string.strip().split("\n")
    coverage = [float(x.strip().split(" ")[0]) for x in cov_string_split]
    cov_timestamps = [(float(x.strip().split(" ")[1]) / 10**3) - start for x in cov_string_split]
    #   path = check_output("grep -F '[quantolic] full path: ' {} | cut -f 4-  -d ' '".format(CATG_OUTPUT_FILE),shell=True)
    return {"coverage":coverage,"timestamps":cov_timestamps,"repeated_paths":0,"inputs":[]}
    

def dump_csv(name,mode,stats):
    with open("".join([CSV_OUTPUT_DIR,"/",name,".",mode,".csv"]),'w') as csvfile:
        csvfile.write("name,mode,cov,time\n")
        cov = stats["coverage"]
        timestamps = stats["timestamps"]
        cumulative_cov = 0
        for i in range(len(cov)):
            cumulative_cov += cov[i]
            line = ",".join([name,mode,str(cumulative_cov),str(timestamps[i])])
            csvfile.write(line + "\n")

def main():
    strategy=argv[1].upper()
    seed(argv[3])
    ntimes = int(argv[2])

    
    if strategy not in Strategies:
        print "Unknown strategy: " + strategy
        print "Valid values are: " + str(Strategies)
        exit(1)

    copy(STRATEGY_CONFIG_FILES[strategy],CONFIG_FILE)

    #[TCAS,SIENA,JTOPAS,BIN_TREE]
    for subject_data in [TCAS,SIENA,BIN_TREE,JTOPAS]:

        #start new counting server
        print "[benchmark] restarting counting server... (not included in the time!) "
        check_output(CMD_RESTART_COUNTING_SERVER,shell=True)

        start = time()
        classname,var_data = subject_data
        stats = {"no":"data"}
        print "[randomcoverage] starting random testing of " + classname

        if strategy == Strategies.RANDOM:
            stats = random_janala(classname,var_data,ntimes,start)
        else:
            stats = heuristic_janala(classname,ntimes,start)

        end = time()
        print "[benchmark] final coverage, max of {} paths ({} distinct) in {} seconds: {}".format(ntimes, len(stats["coverage"]), end - start, sum(stats["coverage"]))
        dump_csv(classname,strategy,stats)

main()
