import java.util.Comparator;
public class CPUBurstCompare implements Comparator<Object> {
    public int compare(Object obj1, Object obj2 ){
        Process p1 = (Process)obj1;
        Process p2 = (Process)obj2;
        if(p1.remainingCPUTime>p2.remainingCPUTime){//use sorted input priority to choose priorities of processes
            return 1;
        }

        else {
            return -1;
        }
    }
}
