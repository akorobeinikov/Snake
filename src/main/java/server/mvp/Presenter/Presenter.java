package server.mvp.Presenter;

import server.mvp.View.IViewServer;
import server.mvp.Model.IModelServer;


class Presenter implements IPresenter{
    IViewServer v;
    IModelServer observed_model;

    public Presenter(IModelServer _m, IViewServer _v)
    {
        v = _v;
        observed_model = _m;

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
                        observed_model.setField(v.getField());
                    }
                    if(code == 2)
                    {
                        v.setOp(1);
                        v.setField(observed_model.getField());
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
        v.setOp(1);
        v.setField(observed_model.getField());
    }

}
