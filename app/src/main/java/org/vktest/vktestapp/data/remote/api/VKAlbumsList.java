package org.vktest.vktestapp.data.remote.api;

import java.util.List;

public class VKAlbumsList {

    private Long count;
    private List<VKAlbum> items;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<VKAlbum> getItems() {
        return items;
    }

    public int photosTotalCount(){
        int total = 0;
        for(VKAlbum album: getItems()){
            total += album.getSize();
        }

        return total;
    }

    public void setItems(List<VKAlbum> items) {
        this.items = items;
    }
}
