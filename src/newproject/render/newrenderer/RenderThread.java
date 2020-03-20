package newproject.render.newrenderer;

/**
 * Created by Luecx on 22.08.2017.
 */
public class RenderThread extends Thread {

    private Renderer renderer;
    private int threadIndex;
    private int chunkIndex = -1;

    public RenderThread(Renderer renderer, int threadIndex) {
        super("RenderThread: " + threadIndex);
        this.renderer = renderer;
        this.threadIndex = threadIndex;
        this.start();
    }

    public void run() {
        try{
            while(true) {
                chunkIndex = renderer.getNextIndex();
                if(!renderer.renderChunk(chunkIndex)){
                    break;
                }
            }
            this.interrupt();
            renderer.threadFinished(threadIndex);
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            if(e instanceof NullPointerException) {
                renderer.stopThreads();
            }
            this.interrupt();
        }
    }
}
