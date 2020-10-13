package server.mvp.Model;


public class BModelServer {
    static IModelServer game_model = new ModelServer();
    public static IModelServer model()
    {
        return game_model;
    }
}
