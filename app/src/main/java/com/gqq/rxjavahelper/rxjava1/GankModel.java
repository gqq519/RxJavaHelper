package com.gqq.rxjavahelper.rxjava1;

import java.util.List;

/**
 * Created by gqq on 17/9/20.
 */

public class GankModel {

    /**
     * error : false
     * results : [{"_id":"59bbe478421aa9118c8262ca","createdAt":"2017-09-15T22:32:24.21Z","desc":"Android上取代HashMap的ArrayMap","images":["http://img.gank.io/a87f186b-47cc-44a4-98c7-a2dd05576fe7"],"publishedAt":"2017-09-20T13:17:38.709Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/088b9383f974","used":true,"who":"Niekon"}]
     */

    private boolean error;
    private List<DataModel> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<DataModel> getResults() {
        return results;
    }

    public void setResults(List<DataModel> results) {
        this.results = results;
    }

    public static class DataModel {
        /**
         * _id : 59bbe478421aa9118c8262ca
         * createdAt : 2017-09-15T22:32:24.21Z
         * desc : Android上取代HashMap的ArrayMap
         * images : ["http://img.gank.io/a87f186b-47cc-44a4-98c7-a2dd05576fe7"]
         * publishedAt : 2017-09-20T13:17:38.709Z
         * source : web
         * type : Android
         * url : http://www.jianshu.com/p/088b9383f974
         * used : true
         * who : Niekon
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
