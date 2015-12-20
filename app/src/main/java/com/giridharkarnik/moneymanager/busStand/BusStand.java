package com.giridharkarnik.moneymanager.busStand;


import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class BusStand {
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private void BusProvider() {

    }
}
