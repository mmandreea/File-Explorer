package partions;

import java.io.File;
import java.io.IOException;
public class GetPartitions {

    public File[] getPartitions(){
        File[] roots=File.listRoots();
        return roots;
    }

    public String[] getPartitionName(File partition) {
        return new String[]{partition.getPath()};
    }


}


