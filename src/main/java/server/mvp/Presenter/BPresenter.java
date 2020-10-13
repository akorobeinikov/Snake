package server.mvp.Presenter;

import server.mvp.View.IViewServer;
import server.mvp.Model.IModelServer;


public class BPresenter {
    static public IPresenter build(IModelServer model, IViewServer view)
    {
        return new Presenter(model, view);
    }
}
