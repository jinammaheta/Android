package com.example.smc1;

import java.util.Comparator;

class GridDataSortingComparator implements Comparator<GridItem> {
    @Override
    public int compare(GridItem t1, GridItem t2) {
        int td1,td2;
        int compareduration ;
        try {
            td1 = Integer.parseInt(t1.getDuration().replace(" mins", ""));
            td2 = Integer.parseInt(t2.getDuration().replace(" mins", ""));
            compareduration = td1 - td2;
            double d1,d2;
            d1 = Double.parseDouble(t1.getDistance().replace(" km", ""));
            d2 = Double.parseDouble(t2.getDistance().replace(" km", ""));
            int comparedistance = (int)(d1 * 10 - d2 * 10);
            if (compareduration == 0) {
                return comparedistance;
            } else {
                return compareduration;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return -1;
        }
    }
}
