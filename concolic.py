import shutil
import os
import subprocess
import platform
import sys
import shlex
import argparse

from datetime import datetime

def getArguments ():
    parser = argparse.ArgumentParser()
    parser.add_argument("--offline", help="Perform concolic testing offline.  An intermediate trace file is generated during the execution of the program. offilne mode results in 2X slowdown that non-offline mode", action="store_true")
    parser.add_argument("-v", "--verbose", help="Print commands that are executed.", action="store_true")
    parser.add_argument("-c", "--coverage", help="Compute detailed coverage by rerunning tests.", action="store_true")
    parser.add_argument("-D", help="JVM options", action="append")
    parser.add_argument("maxIterations", help="Maximum number of times the program under test can be executed.", type=int)
    parser.add_argument("className", help="Java class to be tested.")
    parser.add_argument("arguments", nargs='*', help="Arguments passed to the program under test.")
    args = parser.parse_args()
    return args


catg_tmp_dir = "catg_tmp"
CONCOLIC_OUTPUT_FILE="catg_output"

def concolic (first_input=None):
    cmd1 = "java -Xmx4096M -Xms2048M -Djava.util.logging.manager=janala.utils.MyLogManager -Djanala.loggerClass="+loggerClass+" -Djanala.conf="+catg_home+"catg.conf "+jvmOpts+" -javaagent:\""+catg_home+"lib/iagent.jar\" -cp "+ classpath+" -ea "+yourpgm+" "+arguments
#    print cmd1
    cmd1List = shlex.split(cmd1)
    if verbose:
        print cmd1
    try:
        shutil.rmtree(catg_tmp_dir)
    except: pass
    os.mkdir(catg_tmp_dir)
    os.chdir(catg_tmp_dir)

    print "[janala] performing concolic execution of "+yourpgm
    if first_input is not None:
        with open("inputs","w") as outfile:
            outfile.write(first_input)
            outfile.flush()

    i = 1
    while i <= iters:
        try:
            try:
                with open ("isRealInput", "r") as myfile:
                    data=myfile.read().replace('\n', '')
                print data
            except:
                data = "true"
            if data != "false":
                shutil.copy("inputs", "inputs{}".format(i))
            shutil.copy("inputs", "inputs.old")
        except:
            pass
        try:
            shutil.copy("history", "history.old")
        except:
            pass
        dt = datetime.now()

        try:
            with open("inputs") as infile:
                inputData = infile.read().replace('\n',' ')
        except:
            inputData = "empty"

        print "[Input {} at ({}, {}, {}, {}, {}) : {}]".format(i, dt.day, dt.hour, dt.minute, dt.second, dt.microsecond, inputData)
        sys.stdout.flush()

        with open(CONCOLIC_OUTPUT_FILE,"a") as outfile:
            subprocess.call(cmd1List, shell=windows,stdout=outfile)

        if isOffline:
            print "..."
            cmd2 = "java -Xmx4096M -Xms2048M -Djanala.conf="+catg_home+"catg.conf -Djanala.mainClass="+yourpgm+" -Djanala.iteration="+str(i)+" -cp "+classpath+" -ea janala.interpreters.LoadAndExecuteInstructions"
            if verbose:
                print cmd2
            cmd2List = shlex.split(cmd2)
            subprocess.call(cmd2List, shell=windows)
        i = i + 1
        if os.path.isfile("history") or os.path.isfile("backtrackFlag"):
            pass
        elif i == iters:
            with open("../test.log", 'a') as f:
                f.write("{} ({}) passed\n".format(yourpgm, iters))
            return
        else:
            with open("../test.log", 'a') as f:
                f.write("****************** {} ({}) failed!!!\n".format(yourpgm, iters))
            return
    with open("../test.log", 'a') as f:
        f.write("****************** {} ({}) failed!!!\n".format(yourpgm, iters))



def remove(file):
    try:
        os.remove(file)
    except:
        pass


def rerunTests():
    print "Rerunning tests"
    cmd1 = "java -Xmx4096M -Xms2048M -ea -Djanala.conf="+catg_home+"catg.conf "+jvmOpts+" -javaagent:"+catg_home+"/lib/jacocoagent.jar=append=true,destfile=jacoco.exec -cp "+ classpath+" "+yourpgm+" "+arguments
    cmd1List = shlex.split(cmd1)
    remove('jacoco.exec')
    remove('inputs')
    remove('inputs.bak')
    remove('inputs.old')
    print "[inputs1]"
    if verbose:
        print cmd1
    subprocess.call(cmd1List, shell=windows)
    for filename in os.listdir('.'):
        if filename.startswith('inputs'):
            shutil.copy(filename, "inputs")
            print "["+filename+"]"
            if verbose:
                print cmd1
            subprocess.call(cmd1List, shell=windows)
    os.chdir("..")
    cmd2 = "ant report"
    cmd2List = shlex.split(cmd2)
    subprocess.call(cmd2List, shell=windows)

if platform.system() == "Windows":
    sep = ";"
    windows=True
else:
    sep = ":"
    windows=False
catg_home = os.path.abspath(os.path.dirname(__file__)).replace("\\","/")+"/"
pcp_home = "/home/mateus/workspace/PathConditionsProbability/PathConditionsProbability"
classpath = (catg_home+"out/production/tests"+sep+catg_home+"out/production/janala"+sep+catg_home+"lib/asm-all-3.3.1.jar" +
            sep+catg_home+"lib/trove-3.0.3.jar"+sep+catg_home+"lib/automaton.jar"+sep+catg_home+"lib/iagent.jar" + 
            sep+catg_home+"lib/guava-18.0.jar"+sep+catg_home+"lib/commons-math3-3.5.jar"+sep+catg_home+"lib/antlr4-runtime-4.3.jar" +
            sep+catg_home+"lib/hamcrest-core-1.3.jar"+sep+catg_home+"lib/junit-4.12.jar"+sep+pcp_home+"/target/classes" + sep + pcp_home +
	    (sep+pcp_home+"/target/dependency/").join([jar for jar in os.listdir(pcp_home+"/target/dependency/") if jar.endswith(".jar")]))

def handle_args(args):
    #TODO clean this global
    global loggerClass, iters, yourpgm,isOffline,verbose,arguments,jvmOpts
    
    iters = args.maxIterations
    yourpgm = args.className
    isOffline = args.offline
    verbose = args.verbose
#    print args.D
    if not args.D == None:
        jvmOpts = "-D"+(" -D".join(args.D))
    else:
        jvmOpts = ""
#    print jvmOpts
    if isOffline:
        loggerClass = "janala.logger.FileLogger"
    else:
        loggerClass = "janala.logger.DirectConcolicExecution"
    arguments = ' '.join(args.arguments)

if __name__ == "__main__":
    args = getArguments()
    print args
    handle_args(args)
    concolic()
    if args.coverage:
        rerunTests()
        print "\n\n*********************************************************************************************"
        print "To see detailed coverage information open the file catg_tmp/coverage/index.html in a browser."
        print "*********************************************************************************************\n"

