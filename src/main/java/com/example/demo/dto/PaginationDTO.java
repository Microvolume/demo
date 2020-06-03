package com.example.demo.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;//显示前一页
    private boolean showFirstPage;//显示第一页
    private boolean showNext;//显示下一页
    private boolean showEndPage;//显示最后一页
    private Integer page;//显示的当前页
    private List<Integer> pages=new ArrayList<>();////页码数的集合
    private Integer totalPage;
    public void setPagination(Integer totalCount, Integer page, Integer size) {
        if(totalCount%size==0){
            totalPage=totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        /*下面显示页数的那一坨*/
        this.page=page;
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        if (page==1) {
            showPrevious  = false;
        } else {
            showPrevious  = true;
        }
        // 是否展示下一页
        if (page==totalPage) {
            showNext  = false;
        } else {
            showNext  = true;
        }
        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
