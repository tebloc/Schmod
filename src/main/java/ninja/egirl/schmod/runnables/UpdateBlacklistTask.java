package ninja.egirl.schmod.runnables;

import ninja.egirl.schmod.Schmod;

public class UpdateBlacklistTask implements Runnable {

    private final Schmod instance;

    public UpdateBlacklistTask(Schmod instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        instance.getBlacklistManager().update();
    }
}
