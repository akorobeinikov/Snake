package server.mvp.Presenter;

import server.mvp.View.IViewServer;
import server.mvp.Model.IModelServer;

import java.util.ArrayDeque;


class Presenter implements IPresenter {
    IViewServer v;
    IModelServer observed_model;

    int id;

    static ArrayDeque<Integer> free_ids = new ArrayDeque<>();
    static int global_id = 0;

    public Presenter(IModelServer _m, IViewServer _v)
    {
        v = _v;
        observed_model = _m;

        id = getNewId();

        start();
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
                        observed_model.update(id, v.getDirection());
                    }
                    if(code == 2)
                    {
                        v.setOp(1);
                        v.setCell(observed_model.getBuffer(id));
                    }
                    if(code == -1)
                    {
                        observed_model.removePresenter(id);
                        free_ids.offerLast(id);
                    }
                }
            }
        }.start();

    }

    @Override
    public void update() {
        v.setOp(1);
        v.setCell(observed_model.getBuffer(id));
    }

    private static int getNewId() {
        if (free_ids.isEmpty()) {
            global_id++;
            return global_id-1;
        } else {
            return free_ids.pollFirst();
        }
    }
}
