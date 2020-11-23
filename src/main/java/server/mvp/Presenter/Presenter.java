package server.mvp.Presenter;

import server.mvp.View.IViewServer;
import server.mvp.Model.IModelServer;

import java.util.ArrayDeque;


class Presenter implements IPresenter {
    IViewServer v;
    IModelServer observed_model;
    boolean lock;
    int id;

    static ArrayDeque<Integer> free_ids = new ArrayDeque<>();
    static int global_id = 0;

    public Presenter(IModelServer _m, IViewServer _v)
    {
        v = _v;
        observed_model = _m;
        lock = true;
        id = getNewId();
        start();
        System.out.println(id + " " + global_id);
        observed_model.addPresenter(id, this);
    }

    void start()
    {
        final Presenter p = this;
        new Thread(){
            @Override
            public void run() {
                int code = -2;

                while(code != -1)
                {
                    code = v.getOp();
                    if(code == 1)
                    {
                        observed_model.setCell(id, v.getCell());
                    }
                    if(code == 2)
                    {
                        v.setOp(1);
                        v.setCell(observed_model.getBuffer(id));
                    }
                    if(code == -1)
                    {
                        observed_model.removePresenter(id, p);
                        free_ids.offerLast(id);
                    }
                }
            }
        }.start();

    }

    @Override
    public void update() {
        if (lock) {
            lock = false;
            v.setOp(1);
            v.setCell(observed_model.getBuffer(id));
        }
        lock = true;
    }

    private static int getNewId() {
        global_id++;
        if (free_ids.isEmpty()) {
            return global_id-1;
        } else {
            return free_ids.pollFirst();
        }
    }
}
