package GoRest.Model;

import java.util.List;

public class TodosBody {

    private Meta meta;
    private List<Todos> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Todos> getData() {
        return data;
    }

    public void setData(List<Todos> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TodosBody{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }
}
