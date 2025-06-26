package com.bytebuilder.dto.response.walrus;

//import lombok.Getter;
//import lombok.Setter;


public class NewlyCreated {
    public BlobObject getBlobObject() {
        return blobObject;
    }

    public void setBlobObject(BlobObject blobObject) {
        this.blobObject = blobObject;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    private BlobObject blobObject;
    private int cost;

}
