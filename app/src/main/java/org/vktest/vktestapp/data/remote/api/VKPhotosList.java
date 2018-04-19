package org.vktest.vktestapp.data.remote.api;

import java.util.List;

public class VKPhotosList {

    private Long count;

    private List<VKPhoto> items;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<VKPhoto> getItems() {
        return items;
    }

    public void setItems(List<VKPhoto> items) {
        this.items = items;
    }

}
