package org.tensorflow.demo.list;

public class CookingInfor {
    int board_id;
    String board_name;
    String board_writer;
    String board_content;

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public String getBoard_writer() {
        return board_writer;
    }

    public void setBoard_writer(String board_writer) {
        this.board_writer = board_writer;
    }

    public String getBoard_content() {
        return board_content;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public CookingInfor(int board_id, String board_name, String board_writer, String board_content) {
        this.board_id = board_id;
        this.board_name = board_name;
        this.board_writer = board_writer;
        this.board_content = board_content;
    }
}