package project.studycafe.app.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
// 이건 페이지 그 자체
public class PageMaker {

    private int totalBoardCount; // 총 몇개의 게시판이 있는지 ex)200
    private int currentPage; // 현재 보여주는 페이지
    private int startPage; // 현재 페이지 ex) 보여주는 10개 중에서 맨 처음 // 1,11,12
    private int endPage;    // ex) 10개중에서 맨 마지막 //10,20,30
    private int totalPages; // 페이지의 마지막 ex) 200/10


    private int perPageNum; // 한 페이지에 보여줄 게시판 개수 ex)10
    private int displayPageNum;


    @Builder
    public PageMaker(int totalBoardCount, int currentPage, Integer perPageNum) {
        this.totalBoardCount = totalBoardCount;
        this.currentPage = currentPage;
        this.perPageNum = (perPageNum != null) ? perPageNum : 10;
        this.displayPageNum = 10;
        calcData();
    }

    private void calcData() {
        totalPages = (int) Math.ceil((double) totalBoardCount / perPageNum);

        endPage = ((int) Math.ceil((double) currentPage / displayPageNum)) * displayPageNum;
        startPage = endPage - displayPageNum + 1;

        if (totalPages <= 0){
            totalPages = 1;
        }

        if (endPage > totalPages) {
            endPage = totalPages;
        }

        if (startPage < 1) {
            startPage = 1;
        }
    }

    @Override
    public String toString() {
        return "PageMaker{" +
                "totalBoardCount=" + totalBoardCount +
                ", currentPage=" + currentPage +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                ", totalPages=" + totalPages +
                ", perPageNum=" + perPageNum +
                ", displayPageNum=" + displayPageNum +
                '}';
    }
}
