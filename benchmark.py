import concolic
import random
import ghalton
import math
import numpy 
from sys import argv, exit
from collections import namedtuple,Counter
from time import time
from shutil import copy,move
from os import chdir
from subprocess import check_output,CalledProcessError

#INPUT_FILE="catg_tmp/inputs"
CMD_RESTART_COUNTING_SERVER="../PathConditionsProbability/PathConditionsProbability/restartServer"
STRATEGY_CONFIG_FILES={"RANDOM":"catg.conf.random",
                       "DFS":"catg.conf.dfs",
                       "QUANTOLIC":"catg.conf.quantolic",
                       "MCTS":"catg.conf.mcts-quantolic",
                       "MCTS_NONE":"catg.conf.mcts-none",
                       "MCTS_NOCONST":"catg.conf.mcts-noconst",
                       "MCTS_PROB":"catg.conf.mcts-prob",
                       "QUASIRANDOM":"catg.conf.random",
                       "TREEBUILDING":"catg.conf.treebuilding"}

CONFIG_FILE="catg.conf"
CATG_OUTPUT_FILE="catg_tmp/catg_output"
CSV_OUTPUT_DIR="results"

TCAS=("tests.casestudies.tcas.Driver",[{'type':'bool'} for i in range(3)] + [{'type':'int','lo':-10000,'hi':10000} for i in range(9)])
SIENA=("tests.casestudies.sir.siena.DecoderTest",[{'type':'int','lo':-128,'hi':127} for i in range(5)])
JTOPAS=("tests.casestudies.sir.jtopasV0.TestPluginTokenizer",[{'type':'int','lo':-128,'hi':127} for i in range(20)])
BIN_TREE=("tests.casestudies.sir.binarytree.BinarySearchTreeTest",[{'type':'int','lo':-100,'hi':100} for i in range(5)])

Arguments = namedtuple('Arguments',['maxIterations','className','offline','verbose','D', 'arguments'])
SEEDS=[-431209415] #,942731727) #,299953477,141084159,-968229660)

class Enum(set):
    def __getattr__(self, name):
        if name in self:
            return name
        raise AttributeError

Strategies=Enum(STRATEGY_CONFIG_FILES.keys())


def gen_input_file(inputvars,halton_seq):
    data = []
    var_input = None

    if halton_seq is None:
        for var in inputvars:
            if var['type'] == 'int':
                hi = var['hi']
                lo = var['lo']
                var_input = random.randint(lo,hi)
            elif var['type'] == 'bool':
                var_input = random.randint(0,1)
            else:
                raise Exception("unknown vartype:",var['type'])
            data.append(var_input)
    else:
        quasirandoms = halton_seq.get(1)[0] #[x1,x2...], 0 <= xN <= 1
        assert len(quasirandoms) == len(inputvars)
        for rand,var in zip(quasirandoms,inputvars):
            if var['type'] == 'int':
                hi = var['hi']
                lo = var['lo']
                var_input =  int(math.floor(rand * (hi - lo + 1)) + lo)
            elif var['type'] == 'bool':
                var_input = int(round(rand)) 
            else:
                raise Exception("unknown vartype:",var['type'])
            data.append(var_input)
 
    text = '\n'.join([str(x) for x in data])
    return (data,text)

def entropy(path_bag,iteration):
    #compute histogram of frequencies
    histogram = Counter(path_bag.values())
    numTests = float(iteration)
    
    naiveEstimator = 0.0
    sumFi = 0.0
    numTests = float(numTests)
    for i, count in histogram.items():
        naiveEstimator += 1*count * ((i+1.0)/numTests) * numpy.abs(numpy.log2((i+1.0)/numTests))
        sumFi += count

    millerMadow = naiveEstimator +(sumFi-1.0)/(2*numTests)
    return (naiveEstimator,millerMadow)



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


def random_janala(classname,var_data,ntimes,start,seed,use_quasirandom):
    distinct_path_inputs = []
    distinct_paths = []

    path_counter = Counter()
    repeated_paths = 0

    coverage = []
    cov_timestamps = []
    
    cumulative_coverage = 0
    random.seed(seed)

    if use_quasirandom:
        print len(var_data)
        halton_seq = ghalton.Halton(len(var_data))
	#ghalton doesn't work with negative seeds 
        halton_seq.seed(abs(seed))
    else:
        halton_seq = None

    sorted_var_data = sorted(var_data)
    for i in range(ntimes):
        input_data,text_input = gen_input_file(sorted_var_data,halton_seq)
        path,cov = run_janala_once(classname,text_input)
        path_counter[path] += 1
        
        if path_counter[path] > 1: # we already saw this one
            repeated_paths += 1
        else:
            distinct_path_inputs.append(input_data)
            distinct_paths.append(path)
            coverage.append(cov)
            cov_timestamps.append(time() - start)
            cumulative_coverage += cov
            print "[benchmark:random] cumulative coverage: {}".format(cumulative_coverage)
            if cumulative_coverage >= 1:
                break

        if i % 10 == 0 and i > 0: # compute entropy
            naive,miller = entropy(path_counter,i)
            distinct = len(distinct_paths)
            print "[entropy]: " + ",".join([str(x) for x in [i,distinct,cumulative_coverage,naive,miller]])
            
#        cumulative_coverage_track.append(cumulative_coverage)

    return {"coverage":coverage,"timestamps":cov_timestamps,"repeated_paths":repeated_paths,"inputs":distinct_path_inputs}


def random_janala_without_filtering(classname,var_data,ntimes,start,seed,use_quasirandom,clean_dir=True):
    distinct_path_inputs = []
    repeated_paths = 0
    coverage = []
    cov_timestamps = []
    cumulative_coverage = 0
    sorted_var_data = sorted(var_data)
    random.seed(seed)
    
    if use_quasirandom:
        halton_seq = ghalton.Halton(len(var_data))
	#ghalton doesn't work with negative seeds 
        halton_seq.seed(abs(seed))
    else:
        halton_seq = None
    
    for i in range(ntimes):
        input_data,text_input = gen_input_file(sorted_var_data,halton_seq)

        #remove/backup old log
        try:
            move(CATG_OUTPUT_FILE,CATG_OUTPUT_FILE+".old")
        except: pass
        #run janala
        args = Arguments(maxIterations=1,className=classname,offline=False,verbose=False,D=None,arguments="")
        concolic.handle_args(args)
        concolic.concolic(text_input,clean_folder=clean_dir)
        chdir("..")

        already_covered = True
        try:
            check_output("grep -a -F '[pathfilter] Path filtered!' '{}' ".format(CATG_OUTPUT_FILE),shell=True)
        except CalledProcessError as e:
            assert e.returncode == 1
            already_covered = False

            
        if already_covered:
            repeated_paths += 1
        else:
            cov_string = check_output("grep -a -F 'domain coverage for this path:' '{}' | cut -f 7 -d ' ' ".format(CATG_OUTPUT_FILE),shell=True)
            cov = float(cov_string)
            distinct_path_inputs.append(input_data)
            coverage.append(cov)
            cov_timestamps.append(time() - start)
            cumulative_coverage += cov
            print "[benchmark:random] cumulative coverage: {}".format(cumulative_coverage)
            if cumulative_coverage >= 1:
                break

    return {"coverage":coverage,"timestamps":cov_timestamps,"repeated_paths":repeated_paths,"inputs":distinct_path_inputs}
        


def heuristic_janala(classname,ntimes,start,clean_dir=True):
    #run janala, process output
    args = Arguments(maxIterations=ntimes,className=classname,offline=False,verbose=False,D=None,arguments="")
    concolic.handle_args(args)
    concolic.concolic(clean_folder=clean_dir)
    chdir("..")
    cov_string = check_output("grep -a -F '[quantolic] domain coverage for this path:' '{}' | cut -f 7,9 -d ' ' ".format(CATG_OUTPUT_FILE),shell=True)
    cov_string_split = cov_string.strip().split("\n")
    coverage = [float(x.strip().split(" ")[0]) for x in cov_string_split]
    cov_timestamps = [(float(x.strip().split(" ")[1]) / 10**3) - start for x in cov_string_split]
    #   path = check_output("grep -F '[quantolic] full path: ' {} | cut -f 4-  -d ' '".format(CATG_OUTPUT_FILE),shell=True)
    return {"coverage":coverage,"timestamps":cov_timestamps,"repeated_paths":0,"inputs":[]}
    

def dump_csv(name,mode,stats,seed):
    with open("".join([CSV_OUTPUT_DIR,"/",name,".",mode,".",seed,".csv"]),'w') as csvfile:
        csvfile.write("name,mode,cov,time,seed\n")
        cov = stats["coverage"]
        timestamps = stats["timestamps"]
        cumulative_cov = 0
        for i in range(len(cov)):
            cumulative_cov += cov[i]
            line = ",".join([name,mode,str(cumulative_cov),str(timestamps[i]),seed])
            csvfile.write(line + "\n")

def main():
    strategies=argv[1].upper().split(":")
    ntimes = int(argv[2])

    nstrats = len(strategies)
    assert nstrats == 1 or nstrats == 2

    for strategy in strategies:
        if strategy not in Strategies:
            print "Unknown strategy: " + strategy
            print "Valid values are: " + str(Strategies)
            exit(1)


    strategy = strategies[0]
    secondary_strat = strategies[1] if nstrats == 2 else None
    
    #[TCAS,SIENA,JTOPAS,BIN_TREE]
    for subject_data in [TCAS,SIENA,BIN_TREE,JTOPAS]:
        copy(STRATEGY_CONFIG_FILES[strategy],CONFIG_FILE)
	for seed in SEEDS:
            #start new counting server
	    print "[benchmark] restarting counting server... (not included in the time!) "
            check_output(CMD_RESTART_COUNTING_SERVER,shell=True)

            start = time()
            classname,var_data = subject_data
            stats = {"no":"data"}
            print "[randomcoverage] starting random testing of " + classname

            if strategy == Strategies.RANDOM or strategy == Strategies.QUASIRANDOM:
                use_quasirandom = strategy == Strategies.QUASIRANDOM
                stats = random_janala(classname,var_data,ntimes,start,seed,use_quasirandom)
            elif strategy == Strategies.TREEBUILDING:
                assert secondary_strat in [Strategies.QUANTOLIC,Strategies.MCTS,Strategies.MCTS_NONE,Strategies.MCTS_PROB,Strategies.MCTS_NOCONST]
                concolic.clean_dir()
                # random first to populate the tree...
                random_iterations = 50
                stats_random = random_janala_without_filtering(classname,var_data,random_iterations,start,seed,False,clean_dir=False)
                # ...then select a unexplored path...
                copy(STRATEGY_CONFIG_FILES[secondary_strat],CONFIG_FILE)
                concolic.genIntermediateInputs()
                # ...and finally run quantolic/mcts 
                remaining_iterations = ntimes - random_iterations
                print "[benchmark:treebuilding] Random stage is finished. Starting heuristic search..."
                stats_heuristic = heuristic_janala(classname,ntimes,start, clean_dir=False)
                stats = {"coverage": stats_random['coverage'] + stats_heuristic['coverage'],
                         "timestamps": stats_random['timestamps'] + stats_heuristic['timestamps'],
                         "repeated_paths": stats_random['repeated_paths'],
                         "inputs":stats_random['inputs']}
            else:
                stats = heuristic_janala(classname,ntimes,start)

            end = time()
            print "[benchmark] final coverage, max of {} paths ({} distinct) in {} seconds: {}".format(ntimes, len(stats["coverage"]), end - start, sum(stats["coverage"]))
            dump_csv(classname,strategy,stats,str(seed))
        
main()
