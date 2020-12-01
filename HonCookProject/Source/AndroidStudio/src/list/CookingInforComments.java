package org.tensorflow.demo.list;

public class CookingInforComments {
    int comments_id;
    int board_id;
    String comments_content;
    String user_name;

    public int getComments_id() {
        return comments_id;
    }

    public void setComments_id(int comments_id) {
        this.comments_id = comments_id;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getComments_content() {
        return comments_content;
    }

    public void setComments_content(String comments_content) {
        this.comments_content = comments_content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public CookingInforComments(int comments_id, int board_id, String comments_content, String user_name) {
        this.comments_id = comments_id;
        this.board_id = board_id;
        this.comments_content = comments_content;
        this.user_name = user_name;
    }
}