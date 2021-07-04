package at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("beforeDate")
    @Expose
    private String beforeDate;

    @SerializedName("limit")
    @Expose
    private Integer limit;

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("offset")
    @Expose
    private Integer offset;

    @SerializedName("previous")
    @Expose
    private String previous;

    @SerializedName("sort")
    @Expose
    private String sort;


    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}