package com.example.sarkaribook.Model;

public class Ebook {


//    "id":"1","category_id":"1",
//            "sub_category_id":"1",
//            "title":"Rich Dad Poor Dad",
//            "description":"Rich Dad Poor Dad",
//            "image_url":"https:\/\/manybooks.net\/\/sites\/default\/files\/styles\/220x330sc\/public\/old-covers\/cover-cust-7150.jpg?itok=efUU_jRw",
//            "file_url":"http:\/\/adminapp.tech\/learners\/public\/pdfs\/rich-dad-poor-dad.pdf",
//            "price":"0",
//            "discount":"0",
//            "featured":"1"
    public int id;
    public int category_id;
    public int sub_category_id;
    public String title;
    public String description;
    public String image_url;
    public String file_url;

    public Ebook(int id, int category_id, int sub_category_id, String title, String description, String image_url, String file_url) {
        this.id = id;
        this.category_id = category_id;
        this.sub_category_id = sub_category_id;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.file_url = file_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
