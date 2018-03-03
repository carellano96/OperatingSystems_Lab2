import com.sun.org.apache.xpath.internal.operations.Mult;
import com.sun.org.apache.xpath.internal.operations.Or;
import jdk.nashorn.internal.runtime.SharedPropertyMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.Collections;

public class mainClass {
    static int Listcounter = 0;
    static ArrayList<Process> Processes = new ArrayList<Process>();

    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub
        int argssize = args.length;
        String file0 = args[0];
        String file1=null;
        if (argssize>1){
        file1 = args[1];}
        int verbose=0;
        if (file0.contains("--verbose")){
            verbose=1;
            file0 = file1;
        }
        File file = new File(file0);
        int NumProcesses;
        File file2 = new File("random-numbers");


        Scanner input = new Scanner(file);
        int NumberOfProcesses = Integer.parseInt(input.next());
        NumProcesses = NumberOfProcesses;
        int NumProcessesCounter = 0;
        while (NumProcessesCounter < NumberOfProcesses) {
            int A = Integer.parseInt(input.next());
            int B = Integer.parseInt(input.next());
            int C = Integer.parseInt(input.next());
            int IO = Integer.parseInt(input.next());
            Process TempProcess = new Process(A, B, C, IO, NumProcessesCounter);
            TempProcess.id=NumProcessesCounter;
            Processes.add(TempProcess);
            NumProcessesCounter++;
        }
        ArrayList<Process> UnSortedProcesses = new ArrayList<Process>();
        UnSortedProcesses.addAll(Processes);
        System.out.print("The original input was: ");
        print(UnSortedProcesses,NumberOfProcesses);
        System.out.print("The (sorted) input is: ");
        BubbleSort(Processes, NumProcesses);

        print(Processes,NumberOfProcesses);
        System.out.println();
       FirstComeFirstServe(Processes,verbose);
        System.out.print("\nThe original input was: ");
        print(UnSortedProcesses,NumberOfProcesses);
        System.out.print("The (sorted) input is: ");
        print(Processes,NumberOfProcesses);
        System.out.println();
        RoundRobin(Processes, verbose);
        System.out.print("\nThe original input was: ");
        print(UnSortedProcesses,NumberOfProcesses);
        System.out.print("The (sorted) input is: ");
        print(Processes,NumberOfProcesses);
        System.out.println();
        Uniprogrammed(Processes,verbose);
        System.out.print("\nThe original input was: ");
        print(UnSortedProcesses,NumberOfProcesses);
        System.out.print("The (sorted) input is: ");
        print(Processes,NumberOfProcesses);
        System.out.println();
        PSJF(Processes,verbose);


    }


static void print(ArrayList<Process>Processes, int NumberOfProcesses){
    System.out.print(NumberOfProcesses+" ");
    for (Process temp: Processes){
        System.out.print(temp.arrivalTime+" "+temp.B+" "+temp.totalCPUTime+ " "+ temp.IO+ "   ");
    }
    System.out.println();
}
    static void BubbleSort(ArrayList<Process> Processes, int NumProcesses) {
        int i, j;
        for (i = 0; i < NumProcesses - 1; i++) {
            for (j = 0; j < NumProcesses - i - 1; j++) {
                if (((Processes.get(j).arrivalTime) > (Processes.get(j + 1)).arrivalTime)) {
                    Process temp = Processes.get(j);//switch if youre switching Obj1, Obj2
                    int tempID = temp.id;
                    Processes.set(j, Processes.get(j + 1));
                    int tempID2 = Processes.get(j+1).id;
                    Processes.get(j+1).id=tempID;
                    Processes.set(j + 1, temp);
                    temp.id=tempID2;
                }
            }
        }
    }
static void RoundRobin(ArrayList<Process> processes, int verboseFlag) throws FileNotFoundException{
    int  Time = 0;
    int CPUTimeLeft = 0;
    int IOTimeLeft = 0;
    int finishedProcess = 0;
    int Quantum=2;
    Process runningProcess = null;
    int numOfProcesses = processes.size();//number of processes
    Process process[] = new Process[numOfProcesses];//put the process in an array
    Queue<Process> readyProcesses = new ConcurrentLinkedQueue<Process>();//create a ready queue
    ArrayList<Process> blockProcesses = new ArrayList<Process>();//create an arraylist of block processes
    ArrayList<Process> finishedProcesses = new ArrayList<Process>();//arraylist of finished processes
    ArrayList<Process> unstartedProcesses = new ArrayList<Process>();
    ArrayList<Process> MultipleReady = new ArrayList<Process>();
    //put the stuff into tables to see if theyre unstarted or not//arraylist of unstarted processes
    System.out.println("The scheduling algorithm used was Round Robin\n");

    if (verboseFlag == 1) {
        System.out.println("This detailed printout gives the state and remaining burst for each process\n");
        System.out.print("Before cycle "+Time + ": ");
        for (int i = 0; i < numOfProcesses; i++) {
            System.out.print("      unstarted  0");
        }
        System.out.println();
    }
    for (int i = 0; i < numOfProcesses; i++) {
        Process tmpProcess = (Process) processes.get(i);
        process[i] = new Process(tmpProcess.arrivalTime, tmpProcess.B, tmpProcess.remainingCPUTime, tmpProcess.IO, i);//get tempprocess and make a copy
        process[i].id = tmpProcess.id;
        process[i].sortedInputPriority = i;//create a priority variable
        if (process[i].arrivalTime == 0) {//if the arrival time is 0, then make it ready
            readyProcesses.add(process[i]);
        } else {
            unstartedProcesses.add(process[i]);//else if its after 0, then make it unstarted
        }
    }////////////////////so we are getting ready for the main loop by putting everything in ready or unstarted tables
    //main loop
    while (finishedProcess < numOfProcesses) {//create a loop that checks if all processes are finished
        //first checked unstarted and put unstarted in ready if theyre ready

        for (int i = 0; i < unstartedProcesses.size(); i++) {//loop through unstarted processes first
            if (unstartedProcesses.get(i).arrivalTime ==  Time) {//check in unstarted processeses and see if its time to be ready

                readyProcesses.add(unstartedProcesses.get(i));//if they are, add them to the ready queue
            }
        }
        Time++;
        //now check running processes if there are any
///////////////////here we are checking to see if there ar any running processes we should retrieve
        if (runningProcess == null) {//if there are no running process then retrieve from ready
            //System.out.println("Null and checking if ready");
            runningProcess = readyProcesses.poll();//grab running
            if (runningProcess != null&&runningProcess.CPUBurstTime <= 0) {//if there is a ready process, then give it a burst time
                //System.out.println("found Ready!");
                runningProcess.randomBurstTime();//look at this

            }
        }
        Quantum--;//update quantum


//so we have an array of the processes and a table of where they should be
        //*verbose states*
        if (verboseFlag == 1) {
            System.out.print("Before cycle "+Time + ": ");
            for (int i = 0; i < numOfProcesses; i++) {
                if (blockProcesses.contains(process[i]))
                    System.out.print("        blocked  " + process[i].IOBurstTime);
                else if (readyProcesses.contains(process[i])) System.out.print("          ready  0");
                else if (unstartedProcesses.contains(process[i]) &&  Time <= processes.get(i).arrivalTime)
                    System.out.print("      unstarted  0");
                else if (finishedProcesses.contains(process[i])) System.out.print(   "     terminated  0");
                else System.out.print("        running  " + (1 + Quantum));

            }
            System.out.println("");
        }//////////////////Here we are updating the currently running if we have to//////////////////////
        if (runningProcess != null) {//if running process is there, then run it, and add to the CPUTimeLeft
            runningProcess.run();//decreases remaining cpu time
            // System.out.println("running! "+ runningProcess.id);
            CPUTimeLeft++;
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
        //check the ready processes and modify it accordingly
        if (!readyProcesses.isEmpty()) {//if there are currently ready processes, we add to their waiting time
            for (Process p : readyProcesses) {
                p.Waitingtime++;
            }
        }
        //check block processes, add IOTime if currently blocked
        if (!blockProcesses.isEmpty()) {//if there are currently blocked processes
            IOTimeLeft++;
            int addToReadyNum = 0;
            ArrayList<Process> addToReadyProcess = new ArrayList<Process>();//create add to ready arraylist
            Process[] pArray = blockProcesses.toArray(new Process[0]);//put block processes to array
            for (int i = 0; i < pArray.length; i++) {
                pArray[i].IOBurstTime--;//decrease the bursttime
                pArray[i].IOtime++;//increase time in IO
                if (pArray[i].IOBurstTime <= 0) {
                    addToReadyNum++;//if the burst time is 0, then we are going add them to an about to add to ready list
                    addToReadyProcess.add(pArray[i]);
                    blockProcesses.remove(pArray[i]);
                }
            }
            //now check the add to ready list
            if (addToReadyNum == 1) {//if theres just one, add it
                MultipleReady.add(addToReadyProcess.get(0));
            } else {//else we need to compare the priorities and sort the processes that are highest priority
                Collections.sort(addToReadyProcess, new PriorityCompare());
                for (int i=0;i<addToReadyProcess.size();i++){
                    MultipleReady.add(addToReadyProcess.get(i));
                }
            }
        }
        ////////////here we are updating the running process at the end if quantum is done////////////////////////
        //if the RunningProcess is not null
        if (Quantum<=0){//if the quantum is done
            if (runningProcess!=null){
                if (runningProcess.remainingCPUTime==0){
                    runningProcess.Finishingtime=Time;//completely done, add to finished processes
                    finishedProcesses.add(runningProcess);
                    finishedProcess++;
                } else if (runningProcess.remainingCPUTime != 0 && runningProcess.CPUBurstTime <= 0) {
                    runningProcess.randomIO();//not completely done but the CPUBursttime is done, add to blocked
                    blockProcesses.add(runningProcess);

                }
                else{
                    MultipleReady.add(runningProcess);//still not completely done but there is burst time left
                }
                runningProcess=null;
                Quantum=2;
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
////////////////if quantum is not done then we update it ////////////////////////////
        else {//if quantum is not done
            if (runningProcess != null) {
                //if process finished
                if (runningProcess.remainingCPUTime == 0) {
                    runningProcess.Finishingtime = Time;//if its completely done, add it to done list
                    finishedProcesses.add(runningProcess);
                    finishedProcess++;
                    runningProcess = null;
                    Quantum = 2;
                }
                //if burst time is used up, then block it
                else if (runningProcess.remainingCPUTime != 0 && runningProcess.CPUBurstTime <= 0) {
                    runningProcess.randomIO();
                    blockProcesses.add(runningProcess);
                    Quantum = 2;
                    runningProcess = null;
                }

            } else {//reset quantum and continue on if there is no runningprocess
                Quantum = 2;
            }
        }
        ///////we are sorting the multiple ready list based on input priority//////////
        if (MultipleReady.size()!=1) {
            Collections.sort(MultipleReady, new PriorityCompare());//if not equal to 1, sort the list
        }
//add all multiple ready to readyprocesses and prepare to start over
        readyProcesses.addAll(MultipleReady);
        MultipleReady.clear();

    }
    //sort by the ID now
    IDCompare comparatorByID = new IDCompare();
    Collections.sort(finishedProcesses, comparatorByID);
    RandomOS RandomOS = new RandomOS();
    RandomOS.reset();
//print the info
    System.out.println("");
    int totalTurnAroundTime = 0;
    int totalWaitingTime = 0;
    for(int i=0;i<finishedProcesses.size();i++){
        Process p = finishedProcesses.get(i);
        p.Turnaroundtime = p.Finishingtime-p.arrivalTime;
        totalTurnAroundTime += p.Turnaroundtime;
        totalWaitingTime += p.Waitingtime;
        System.out.println("Process "+p.id+":");
        System.out.print("              (A,B,C,IO) = ");
        p.printOut();
        System.out.println("");
        System.out.print("              Finishing time: ");
        System.out.println(p.Finishingtime);
        System.out.print("              Turnaround time:  ");
        System.out.println(p.Turnaroundtime);
        System.out.print("              I/O time: ");
        System.out.println(p.IOtime);
        System.out.print("              Waiting time: ");
        System.out.println(p.Waitingtime);
    }
    System.out.println("Summing Data:");
    System.out.println("              Finishing time: "+Time);
    System.out.printf("              CPU Utilization: %.6f\n",(double)CPUTimeLeft/Time);
    System.out.printf("              I/O Utilization: %.6f\n",(double)IOTimeLeft/Time);
    System.out.printf("              Throughput: %.6f processes per hundred  Times\n",(double)finishedProcesses.size()*100/Time);
    System.out.println("              Average turnaround time: "+(double)totalTurnAroundTime/finishedProcesses.size());
    System.out.println("              Average waiting time: "+(double)totalWaitingTime/finishedProcesses.size());

}




    static void FirstComeFirstServe(ArrayList<Process> processes, int verboseFlag) throws FileNotFoundException {
        int  Time = 0;
        int CPUTimeLeft = 0;
        int IOTimeLeft = 0;
        int finishedProcess = 0;
        Process runningProcess = null;
        int numOfProcesses = processes.size();//number of processes
        Process process[] = new Process[numOfProcesses];//put the process in an array
        Queue<Process> readyProcesses = new ConcurrentLinkedQueue<Process>();//create a ready queue
        ArrayList<Process> blockProcesses = new ArrayList<Process>();//create an arraylist of block processes
        ArrayList<Process> finishedProcesses = new ArrayList<Process>();//arraylist of finished processes
        ArrayList<Process> unstartedProcesses = new ArrayList<Process>();
        //put the stuff into tables to see if theyre unstarted or not//arraylist of unstarted processes
        System.out.println("The scheduling algorithm used was First Come First Serve\n");

        if (verboseFlag == 1) {
            System.out.println("This detailed printout gives the state and remaining burst for each process\n");
            System.out.print("Before cycle "+Time + ": ");
            for (int i = 0; i < numOfProcesses; i++) {
                System.out.print("     unstarted  0");
            }
            System.out.println();
        }
        for (int i = 0; i < numOfProcesses; i++) {
            Process tmpProcess = (Process) processes.get(i);
            process[i] = new Process(tmpProcess.arrivalTime, tmpProcess.B, tmpProcess.remainingCPUTime, tmpProcess.IO, i);//get tempprocess and make a copy
            process[i].id = tmpProcess.id;
            process[i].sortedInputPriority = i;//create a priority variable
            if (process[i].arrivalTime == 0) {//if the arrival time is 0, then make it ready
                readyProcesses.add(process[i]);
            } else {
                unstartedProcesses.add(process[i]);//else if its after 0, then make it unstarted
            }
        }//so we are getting ready for the main loop by putting everything in their respective tables
        //main loop
        while (finishedProcess < numOfProcesses) {//create a loop that checks if all processes are finished
            //first checked unstarted and put unstarted in ready if theyre ready

            for (int i = 0; i < unstartedProcesses.size(); i++) {//loop through unstarted processes first
                if (unstartedProcesses.get(i).arrivalTime ==  Time) {//check in unstarted processeses and see if its time to be ready
                    readyProcesses.add(unstartedProcesses.get(i));//if they are, add them to the ready queue
                }
            }
             Time++;
            //now check running processes if there are any

            if (runningProcess == null) {//if there are no running process then retrieve from ready
                runningProcess = readyProcesses.poll();//grab running
                if (runningProcess != null) {//if there is a ready process, then give it a burst time
                    runningProcess.randomBurstTime();//look at this

                }
            }
            if (runningProcess != null) {//if running process is there, then run it, and add to the CPUTimeLeft
                runningProcess.run();//decreases remaining cpu time
                CPUTimeLeft++;
            }
            //*verbose states*
            if (verboseFlag == 1) {
                System.out.print("Before cycle "+Time + ": ");
                for (int i = 0; i < numOfProcesses; i++) {
                    if (blockProcesses.contains(process[i]))
                        System.out.print("       blocked  " + process[i].IOBurstTime);
                    else if (readyProcesses.contains(process[i])) System.out.print("         ready  0");
                    else if (unstartedProcesses.contains(process[i]) &&  Time <= processes.get(i).arrivalTime)
                        System.out.print("     unstarted  0");
                    else if (finishedProcesses.contains(process[i])) System.out.print(   "    terminated  0");
                    else System.out.print("       running  " + (1 + process[i].CPUBurstTime));

                }
                System.out.println("");
            }

            //check the ready processes and modify it accordingly
            if (!readyProcesses.isEmpty()) {//if there are currently ready processes, we add to their waiting time
                for (Process p : readyProcesses) {
                    p.Waitingtime++;
                }
            }
            //check block processes, add IOTime if currently blocked
            if (!blockProcesses.isEmpty()) {//if there are currently blocked processes
                IOTimeLeft++;
                int addToReadyNum = 0;
                ArrayList<Process> addToReadyProcess = new ArrayList<Process>();//create add to ready arraylist
                Process[] pArray = blockProcesses.toArray(new Process[0]);//put block processes to array
                for (int i = 0; i < pArray.length; i++) {
                    pArray[i].IOBurstTime--;//decrease the bursttime
                    pArray[i].IOtime++;//increase time in IO
                    if (pArray[i].IOBurstTime == 0) {
                        addToReadyNum++;//if the burst time is 0, then we are going add them to an about to add to ready list
                        addToReadyProcess.add(pArray[i]);
                        blockProcesses.remove(pArray[i]);
                    }
                }
                //now check the add to ready list
                if (addToReadyNum == 1) {//if theres just one, add it
                    readyProcesses.add(addToReadyProcess.get(0));
                } else {//else we need to compare the priorities and sort the processes that are highest priority
                    PriorityCompare priorityCompare = new PriorityCompare();
                    Collections.sort(addToReadyProcess, priorityCompare);
                    readyProcesses.addAll(addToReadyProcess);//add all processes after sorted by priority
                }
            }
            //if the RunningProcess is not null
            if (runningProcess != null) {
                //if process terminated, then make its finishing time the current time and add it to finishprocesses
                if (runningProcess.remainingCPUTime == 0) {
                    runningProcess.Finishingtime =  Time;
                    finishedProcesses.add(runningProcess);
                    finishedProcess++;
                    runningProcess = null;//make runningprocess null
                }
                //if process not finished but burst time is there then block
                else if (runningProcess.remainingCPUTime != 0 && runningProcess.CPUBurstTime <= 0) {
                    runningProcess.randomIO();
                    blockProcesses.add(runningProcess);
                    runningProcess = null;
                }
            }
        }
        //sort by the ID now
        IDCompare comparatorByID = new IDCompare();
        Collections.sort(finishedProcesses, comparatorByID);
        RandomOS RandomOS = new RandomOS();
        RandomOS.reset();
//print the info
       System.out.println("");
        int totalTurnAroundTime = 0;
        int totalWaitingTime = 0;
        for(int i=0;i<finishedProcesses.size();i++){
            Process p = finishedProcesses.get(i);
            p.Turnaroundtime = p.Finishingtime-p.arrivalTime;
            totalTurnAroundTime += p.Turnaroundtime;
            totalWaitingTime += p.Waitingtime;

            System.out.println("Process "+p.id+":");
            System.out.print("              (A,B,C,IO) = ");
            p.printOut();
            System.out.println("");
            System.out.print("              Finishing time: ");
            System.out.println(p.Finishingtime);
            System.out.print("              Turnaround time:  ");
            System.out.println(p.Turnaroundtime);
            System.out.print("              I/O time: ");
            System.out.println(p.IOtime);
            System.out.print("              Waiting time: ");
            System.out.println(p.Waitingtime);
        }
        System.out.println("Summing Data:");
        System.out.println("              Finishing time: "+Time);
        System.out.printf("              CPU Utilization: %.6f\n",(double)CPUTimeLeft/Time);
        System.out.printf("              I/O Utilization: %.6f\n",(double)IOTimeLeft/Time);
        System.out.printf("              Throughput: %.6f processes per hundred  Times\n",(double)finishedProcesses.size()*100/Time);
        System.out.println("              Average turnaround time: "+(double)totalTurnAroundTime/finishedProcesses.size());
        System.out.println("              Average waiting time: "+(double)totalWaitingTime/finishedProcesses.size());

    }


    static void Uniprogrammed(ArrayList<Process> processes, int verboseFlag) throws FileNotFoundException {
        int Time = 0;
        int CPUTimeLeft = 0;
        int IOTimeLeft = 0;
        int finishedProcess = 0;
        boolean done=true;
        Process runningProcess = null;
        int numOfProcesses = processes.size();//number of processes
        Process process[] = new Process[numOfProcesses];//put the process in an array
        Queue<Process> readyProcesses = new ConcurrentLinkedQueue<Process>();//create a ready queue
        ArrayList<Process> blockProcesses = new ArrayList<Process>();//create an arraylist of block processes
        ArrayList<Process> finishedProcesses = new ArrayList<Process>();//arraylist of finished processes
        ArrayList<Process> unstartedProcesses = new ArrayList<Process>();
        //put the stuff into tables to see if theyre unstarted or not//arraylist of unstarted processes
        System.out.println("The scheduling algorithm used was Uniprogramming\n");

        if (verboseFlag == 1) {
            System.out.println("This detailed printout gives the state and remaining burst for each process\n");
            System.out.print("Before cycle " + Time + ": ");
            for (int i = 0; i < numOfProcesses; i++) {
                System.out.print("     unstarted  0");
            }
            System.out.println();
        }
        for (int i = 0; i < numOfProcesses; i++) {
            Process tmpProcess = (Process) processes.get(i);
            process[i] = new Process(tmpProcess.arrivalTime, tmpProcess.B, tmpProcess.remainingCPUTime, tmpProcess.IO, i);//get tempprocess and make a copy
            process[i].id = tmpProcess.id;
            process[i].sortedInputPriority = i;//create a priority variable
            if (process[i].arrivalTime == 0) {//if the arrival time is 0, then make it ready
                readyProcesses.add(process[i]);
            } else {
                unstartedProcesses.add(process[i]);//else if its after 0, then make it unstarted
            }
        }//so we are getting ready for the main loop by putting everything in their respective tables
        //main loop
            while (finishedProcess < numOfProcesses) {
                for (int i = 0; i < unstartedProcesses.size(); i++) {//loop through unstarted processes first
                    if (unstartedProcesses.get(i).arrivalTime == Time) {//check in unstarted processeses and see if its time to be ready
                        readyProcesses.add(unstartedProcesses.get(i));//if they are, add them to the ready queue
                    }
                }
                Time++;
                if (runningProcess==null&done==true){
                    done=false;
                    runningProcess=readyProcesses.poll();
                    if (runningProcess!=null){
                        runningProcess.randomBurstTime();;
                    }
                }
                if (runningProcess!=null){
                    runningProcess.run();
                    CPUTimeLeft++;
                }
                if (verboseFlag == 1) {
                    System.out.print("Before cycle "+Time + ": ");
                    for (int i = 0; i < numOfProcesses; i++) {
                        if (blockProcesses.contains(process[i]))
                            System.out.print("       blocked  " + process[i].IOBurstTime);
                        else if (readyProcesses.contains(process[i])) System.out.print("         ready  0");
                        else if (unstartedProcesses.contains(process[i]) &&  Time <= processes.get(i).arrivalTime)
                            System.out.print("     unstarted  0");
                        else if (finishedProcesses.contains(process[i])) System.out.print(   "    terminated  0");
                        else System.out.print("       running  " + (1 + process[i].CPUBurstTime));

                    }
                    System.out.println("");
                }
                if (!readyProcesses.isEmpty()) {//if there are currently ready processes, we add to their waiting time
                    for (Process p : readyProcesses) {
                        p.Waitingtime++;
                    }
                }

                if (!blockProcesses.isEmpty()){
                    IOTimeLeft++;
                    for (Process temp:blockProcesses) {
                        temp.IOBurstTime--;//decrease the bursttime
                        temp.IOtime++;//increase time in IO
                        if (temp.IOBurstTime == 0) {

                            runningProcess=temp;
                            runningProcess.randomBurstTime();;
                            blockProcesses.clear();
                            break;
                        }
                    }
                }
                else if (runningProcess != null) {
                    //if process terminated, then make its finishing time the current time and add it to finishprocesses
                    if (runningProcess.remainingCPUTime == 0) {
                        runningProcess.Finishingtime =  Time;
                        finishedProcesses.add(runningProcess);
                        finishedProcess++;
                        done=true;
                        runningProcess = null;//make runningprocess null
                    }
                    //if process not finished but burst time is there then block
                    else if (runningProcess.remainingCPUTime != 0 && runningProcess.CPUBurstTime <= 0) {
                        runningProcess.randomIO();
                        blockProcesses.add(runningProcess);
                        runningProcess = null;
                    }
                }


            }

            IDCompare comparatorByID = new IDCompare();
            Collections.sort(finishedProcesses, comparatorByID);
            RandomOS RandomOS = new RandomOS();
            RandomOS.reset();
//print the info
            System.out.println("");
            int totalTurnAroundTime = 0;
            int totalWaitingTime = 0;
            for (int i = 0; i < finishedProcesses.size(); i++) {
                Process p = finishedProcesses.get(i);
                p.Turnaroundtime = p.Finishingtime - p.arrivalTime;
                totalTurnAroundTime += p.Turnaroundtime;
                totalWaitingTime += p.Waitingtime;

                System.out.println("Process " + p.id + ":");
                System.out.print("              (A,B,C,IO) = ");
                p.printOut();
                System.out.println("");
                System.out.print("              Finishing time: ");
                System.out.println(p.Finishingtime);
                System.out.print("              Turnaround time:  ");
                System.out.println(p.Turnaroundtime);
                System.out.print("              I/O time: ");
                System.out.println(p.IOtime);
                System.out.print("              Waiting time: ");
                System.out.println(p.Waitingtime);
            }
            System.out.println("Summing Data:");
            System.out.println("              Finishing time: " + Time);
            System.out.printf("              CPU Utilization: %.6f\n", (double) CPUTimeLeft / Time);
            System.out.printf("              I/O Utilization: %.6f\n", (double) IOTimeLeft / Time);
            System.out.printf("              Throughput: %.6f processes per hundred  Times\n", (double) finishedProcesses.size() * 100 / Time);
            System.out.println("              Average turnaround time: " + (double) totalTurnAroundTime / finishedProcesses.size());
            System.out.println("              Average waiting time: " + (double) totalWaitingTime / finishedProcesses.size());

        }
        static void PSJF(ArrayList<Process> processes, int verboseFlag){

            int  Time = 0;
            int CPUTimeLeft = 0;
            int IOTimeLeft = 0;
            int finishedProcess = 0;
            Process runningProcess = null;
            int numOfProcesses = processes.size();//number of processes
            Process process[] = new Process[numOfProcesses];//put the process in an array
            Queue<Process> readyProcesses = new ConcurrentLinkedQueue<Process>();//create a ready queue
            ArrayList<Process> blockProcesses = new ArrayList<Process>();//create an arraylist of block processes
            ArrayList<Process> finishedProcesses = new ArrayList<Process>();//arraylist of finished processes
            ArrayList<Process> unstartedProcesses = new ArrayList<Process>();
            ArrayList<Process> ShortestCPUTimeList = new ArrayList<Process>();
            //put the stuff into tables to see if theyre unstarted or not//arraylist of unstarted processes
            System.out.println("The scheduling algorithm used was PSJF\n");

            if (verboseFlag == 1) {
                System.out.println("This detailed printout gives the state and remaining burst for each process\n");
                System.out.print("Before cycle "+Time + ": ");
                for (int i = 0; i < numOfProcesses; i++) {
                    System.out.print("     unstarted  0");
                }
                System.out.println();
            }
            for (int i = 0; i < numOfProcesses; i++) {
                Process tmpProcess = (Process) processes.get(i);
                process[i] = new Process(tmpProcess.arrivalTime, tmpProcess.B, tmpProcess.remainingCPUTime, tmpProcess.IO, i);//get tempprocess and make a copy
                process[i].id = tmpProcess.id;
                process[i].sortedInputPriority = i;//create a priority variable
                if (process[i].arrivalTime == 0) {//if the arrival time is 0, then make it ready
                    ShortestCPUTimeList.add(process[i]);
                    Collections.sort(ShortestCPUTimeList,new CPUBurstCompare());

                } else {
                    unstartedProcesses.add(process[i]);//else if its after 0, then make it unstarted
                }
            }//so we are getting ready for the main loop by putting everything in their respective tables
            //main loop
            while (finishedProcess < numOfProcesses) {//create a loop that checks if all processes are finished
                //first checked unstarted and put unstarted in ready if theyre ready

                for (int i = 0; i < unstartedProcesses.size(); i++) {//loop through unstarted processes first
                    if (unstartedProcesses.get(i).arrivalTime ==  Time) {//check in unstarted processeses and see if its time to be ready
                        ShortestCPUTimeList.add(unstartedProcesses.get(i));//if they are, add them to the ready queue
                        Collections.sort(ShortestCPUTimeList,new PriorityCompare());
                        //readyProcesses.add(ShortestCPUTimeList.get(ShortestCPUTimeList.size()-1));
                        //ShortestCPUTimeList.remove(ShortestCPUTimeList.get(ShortestCPUTimeList.size()-1));


                    }
                }
                Time++;
                Collections.sort(ShortestCPUTimeList,new CPUBurstCompare());
                if (!ShortestCPUTimeList.isEmpty()) {

                    if (runningProcess!=null){
                    Process shortest = runningProcess;
                    Collections.sort(ShortestCPUTimeList, new CPUBurstCompare());
                    for (Process shortestest : ShortestCPUTimeList) {//compare cpuburst times

                        if (shortest.remainingCPUTime > shortestest.remainingCPUTime) {

                            shortest = shortestest;
                        }
                    }
                    if (!shortest.equals(runningProcess)) {//if it changed, update

                        ShortestCPUTimeList.remove(shortest);
                        ShortestCPUTimeList.add(runningProcess);
                        Collections.sort(ShortestCPUTimeList, new CPUBurstCompare());
                        runningProcess = shortest;
                        readyProcesses.remove(runningProcess);
                        if (runningProcess.CPUBurstTime == 0) {
                            runningProcess.randomBurstTime();
                        }
                        readyProcesses.clear();
                        readyProcesses.addAll(ShortestCPUTimeList);


                    }//else continue on
                }
                else if (runningProcess==null){
                        Collections.sort(ShortestCPUTimeList, new CPUBurstCompare());
                        Process shortest = ShortestCPUTimeList.get(0);
                        for (Process shortestest : ShortestCPUTimeList) {//compare cpuburst times
                            if (shortest.remainingCPUTime > shortestest.remainingCPUTime) {
                                shortest = shortestest;
                            }
                            else if (shortest.remainingCPUTime==shortestest.remainingCPUTime){
                                if (shortest.id>shortestest.id){
                                    shortest=shortestest;
                                }
                            }
                        }
                            ShortestCPUTimeList.remove(shortest);
                            Collections.sort(ShortestCPUTimeList, new CPUBurstCompare());
                            runningProcess = shortest;
                        readyProcesses.remove(runningProcess);
                        if (runningProcess.CPUBurstTime == 0) {
                                runningProcess.randomBurstTime();
                            }
                        readyProcesses.clear();
                        readyProcesses.addAll(ShortestCPUTimeList);

                        //else continue on

                    }

                }
                //now check running processes if there are any
                if (runningProcess == null) {//if there are no running process then retrieve from ready
                    runningProcess = readyProcesses.poll();//grab running
                    ShortestCPUTimeList.remove(runningProcess);
                    if (runningProcess != null&&runningProcess.CPUBurstTime==0) {//if there is a ready process, then give it a burst time
                        runningProcess.randomBurstTime();//look at this

                    }
                }


                if (runningProcess != null) {//if running process is there, then run it, and add to the CPUTimeLeft
                    runningProcess.run();//decreases remaining cpu time
                    CPUTimeLeft++;


                }
                //*verbose states*
                if (verboseFlag == 1) {
                    System.out.print("Before cycle "+Time + ": ");
                    for (int i = 0; i < numOfProcesses; i++) {
                        if (blockProcesses.contains(process[i]))
                            System.out.print("       blocked  " + process[i].IOBurstTime);
                        else if (readyProcesses.contains(process[i])||ShortestCPUTimeList.contains(process[i])) System.out.print("         ready  "+process[i].CPUBurstTime);
                        else if (unstartedProcesses.contains(process[i]) &&  Time <= processes.get(i).arrivalTime)
                            System.out.print("     unstarted  0");
                        else if (finishedProcesses.contains(process[i])) System.out.print(   "    terminated  0");
                        else System.out.print("       running  " + (1 + process[i].CPUBurstTime));

                    }
                    System.out.println("");
                }



                //check the ready processes and modify it accordingly
                if (!readyProcesses.isEmpty()) {//if there are currently ready processes, we add to their waiting time
                    for (Process p : readyProcesses) {
                        p.Waitingtime++;
                    }
                }
                if (!ShortestCPUTimeList.isEmpty()){
                    for (Process p: ShortestCPUTimeList){
                        if (!readyProcesses.contains(p)){
                        p.Waitingtime++;}
                    }
                }
                //check block processes, add IOTime if currently blocked
                if (!blockProcesses.isEmpty()) {//if there are currently blocked processes
                    IOTimeLeft++;
                    int addToReadyNum = 0;
                    ArrayList<Process> addToReadyProcess = new ArrayList<Process>();//create add to ready arraylist
                    Process[] pArray = blockProcesses.toArray(new Process[0]);//put block processes to array
                    for (int i = 0; i < pArray.length; i++) {
                        pArray[i].IOBurstTime--;//decrease the bursttime
                        pArray[i].IOtime++;//increase time in IO
                        if (pArray[i].IOBurstTime == 0) {
                            addToReadyNum++;//if the burst time is 0, then we are going add them to an about to add to ready list
                            addToReadyProcess.add(pArray[i]);
                            blockProcesses.remove(pArray[i]);
                        }
                    }
                    //now check the add to ready list
                    if (addToReadyNum == 1) {//if theres just one, add it
                        ShortestCPUTimeList.add(addToReadyProcess.get(0));
                    } else {//else we need to compare the priorities and sort the processes that are highest priority
                        PriorityCompare priorityCompare = new PriorityCompare();
                        Collections.sort(addToReadyProcess, priorityCompare);
                        ShortestCPUTimeList.addAll(addToReadyProcess);
                        Collections.sort(ShortestCPUTimeList,new CPUBurstCompare());
                    }
                }

                if (runningProcess != null) {
                    //if process terminated, then make its finishing time the current time and add it to finishprocesses
                    if (runningProcess.remainingCPUTime == 0) {
                        runningProcess.Finishingtime =  Time;
                        finishedProcesses.add(runningProcess);
                        finishedProcess++;
                        runningProcess = null;//make runningprocess null
                    }
                    //if process not finished but burst time is there then block
                    else if (runningProcess.remainingCPUTime != 0 && runningProcess.CPUBurstTime <= 0) {
                        runningProcess.randomIO();
                        blockProcesses.add(runningProcess);

                        runningProcess = null;
                    }
                }

            }
            //sort by the ID now
            IDCompare comparatorByID = new IDCompare();
            Collections.sort(finishedProcesses, comparatorByID);
            RandomOS RandomOS = new RandomOS();
            RandomOS.reset();
//print the info
            System.out.println("");
            int totalTurnAroundTime = 0;
            int totalWaitingTime = 0;
            for(int i=0;i<finishedProcesses.size();i++){
                Process p = finishedProcesses.get(i);
                p.Turnaroundtime = p.Finishingtime-p.arrivalTime;
                totalTurnAroundTime += p.Turnaroundtime;
                totalWaitingTime += p.Waitingtime;

                System.out.println("Process "+p.id+":");
                System.out.print("              (A,B,C,IO) = ");
                p.printOut();
                System.out.println("");
                System.out.print("              Finishing time: ");
                System.out.println(p.Finishingtime);
                System.out.print("              Turnaround time:  ");
                System.out.println(p.Turnaroundtime);
                System.out.print("              I/O time: ");
                System.out.println(p.IOtime);
                System.out.print("              Waiting time: ");
                System.out.println(p.Waitingtime);
            }
            System.out.println("Summing Data:");
            System.out.println("              Finishing time: "+Time);
            System.out.printf("              CPU Utilization: %.6f\n",(double)CPUTimeLeft/Time);
            System.out.printf("              I/O Utilization: %.6f\n",(double)IOTimeLeft/Time);
            System.out.printf("              Throughput: %.6f processes per hundred  Times\n",(double)finishedProcesses.size()*100/Time);
            System.out.println("              Average turnaround time: "+(double)totalTurnAroundTime/finishedProcesses.size());
            System.out.println("              Average waiting time: "+(double)totalWaitingTime/finishedProcesses.size());




        }



        }



