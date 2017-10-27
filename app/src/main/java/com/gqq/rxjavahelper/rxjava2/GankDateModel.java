package com.gqq.rxjavahelper.rxjava2;

import java.util.List;

/**
 * Created by gqq on 17/9/21.
 */

public class GankDateModel {

    /**
     * error : false
     * results : ["2017-09-20","2017-09-19"]
     */

    private boolean error;
    private List<String> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
