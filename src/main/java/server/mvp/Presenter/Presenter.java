package server.mvp.Presenter;

import server.mvp.View.IViewServer;
import server.mvp.Model.IModelServer;


class Presenter implements IPresenter{
    IViewServer v;
    IModelServer observed_model;
    boolean lock;

    public Presenter(IModelServer _m, IViewServer _v)
    {
        v = _v;
        observed_model = _m;
        lock = true;
        start();
        observed_model.addPresenter(this);
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
                        observed_model.setCell(v.getCell());
                    }
                    if(code == 2)
                    {
                        v.setOp(1);
                        v.setCell(observed_model.getBuffer());
                    }
                    if(code == -1)
                    {
                        observed_model.removePresenter(p);
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
            v.setCell(observed_model.getBuffer());
        }
        lock = true;
    }

}
