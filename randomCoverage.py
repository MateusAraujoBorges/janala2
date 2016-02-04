import concolic
from sys import argv
from random import seed, randint
from collections import namedtuple
from time import time
from shutil import copy
from os import chdir
from subprocess import check_output

#INPUT_FILE="catg_tmp/inputs"
CONFIG_FILE_RANDOM="catg.conf.random"
CONFIG_FILE="catg.conf"
CATG_OUTPUT_FILE="catg_tmp/catg_output"


TCAS=("tests.casestudies.tcas.Driver",[{'type':'bool'} for i in range(3)] + [{'type':'int','lo':-10000,'hi':10000} for i in range(9)])
SIENA=("tests.casestudies.sir.siena.DecoderTest",[{'type':'int','lo':-128,'hi':127} for i in range(5)])
JTOPAS=("tests.casestudies.sir.jtopasV0.TestPluginTokenizer",[{'type':'int','lo':-128,'hi':127} for i in range(20)])
BIN_TREE=("tests.casestudies.sir.binarytree.BinarySearchTreeTest",[{'type':'int','lo':-100,'hi':100} for i in range(5)])

Arguments = namedtuple('Arguments',['maxIterations','className','offline','verbose','D', 'arguments'])


def gen_input_file(inputvars):
    data = []
    for var in inputvars:
        if var['type'] == 'int':
            hi = var['hi']
            lo = var['lo']
            var_input = randint(lo,hi)
            data.append(var_input)
        elif var['type'] == 'bool':
            var_input = randint(0,1)

 #   with open(INPUT_FILE,'w') as input_file:
        text = '\n'.join([str(x) for x in data])
 #       input_file.write(content)
    return (data,text)

def run_janala(classname,first_input):
    args = Arguments(maxIterations=1,className=classname,offline=False,verbose=False,D=None,arguments="")
    concolic.handle_args(args)
    concolic.concolic(first_input)
    chdir("..")
    cov_string = check_output("grep -F '[quantolic] domain coverage for this path:' '{}' | cut -f 7 -d ' ' ".format(CATG_OUTPUT_FILE),shell=True)
    cov = float(cov_string)
    path = check_output("grep -F '[quantolic] full path: ' {} | cut -f 4-  -d ' '".format(CATG_OUTPUT_FILE),shell=True)
    return (path,cov)


def main():
    seed(argv[2])
    times = int(argv[1])
    copy(CONFIG_FILE_RANDOM,CONFIG_FILE)

    #[TCAS,SIENA,JTOPAS,BIN_TREE]
    for subject_data in [BIN_TREE]:
        start = time()
        classname,var_data = subject_data
        print "[randomcoverage] starting random testing of " + classname
        distinct_path_inputs = []
        distinct_paths = []
        coverage = []

        path_set = set()
        repeated_paths = 0
        
        for i in range(times):
            input_data,text_input = gen_input_file(var_data)
            path,cov = run_janala(classname,text_input)

            if path in path_set:
                repeated_paths += 1
            else:
                path_set.add(path)
                distinct_path_inputs.append(input_data)
                distinct_paths.append(path)
                coverage.append(cov)

        end = time()
        print "[randomcoverage] final coverage after {} paths ({} distinct) and {} seconds: {}".format(times, times - repeated_paths, end - start, sum(coverage)) 


main()
