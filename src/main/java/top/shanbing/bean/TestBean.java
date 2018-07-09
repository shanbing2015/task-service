package top.shanbing.bean;

import java.io.Serializable;
import java.util.List;

public class TestBean implements Serializable {
    public int id;
    public String name;

    public List<TestBean> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TestBean> getList() {
        return list;
    }

    public void setList(List<TestBean> list) {
        this.list = list;
    }

    public TestBean() {
    }

    public TestBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
