package com.multi.smore.news.model.vo;

import java.util.Comparator;
import java.util.Date;

public class NewsComparator implements Comparator<News> {  
    @Override  
    public int compare(News first, News second) {
        Date firstValue = first.getPubDate();
        Date secondValue = second.getPubDate();
        int result = firstValue.compareTo(secondValue);
        
        if (result < 0) {  
            return -1;  
        } else if (result > 0) {  
            return 1;  
        } else {  
            return 0;  
        }  
    }

}
