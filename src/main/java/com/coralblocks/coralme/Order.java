/* 
 * Copyright 2023 (c) CoralBlocks - http://www.coralblocks.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.coralblocks.coralme;

import java.util.ArrayList;
import java.util.List;

import com.coralblocks.coralme.util.DoubleUtils;
import com.coralblocks.coralme.CancelReason;
import com.coralblocks.coralme.CancelRejectReason;
import com.coralblocks.coralme.ExecuteSide;
import com.coralblocks.coralme.ReduceRejectReason;
import com.coralblocks.coralme.RejectReason;
import com.coralblocks.coralme.Side;
import com.coralblocks.coralme.TimeInForce;
import com.coralblocks.coralme.Type;

public class Order {

    final static String EMPTY_CLIENT_ORDER_ID = "NULL";

    public final static int CLIENT_ORDER_ID_MAX_LENGTH = 64;

    private final List<OrderListener> listeners = new ArrayList<OrderListener>(64);

    private Side side;

    private long originalSize;

    private long totalSize;

    private long executedSize;

    private PriceLevel priceLevel;

    private long clientId;

    private final StringBuilder clientOrderId = new StringBuilder(CLIENT_ORDER_ID_MAX_LENGTH);

    private long price;

    private long acceptTime;

    private long restTime;

    private long cancelTime;

    private long rejectTime;

    private long reduceTime;

    private long executeTime;

    private long id;

    private String security;

    private TimeInForce tif;

    private Type type;

    Order next = null;

    Order prev = null;

    private boolean isResting;

    private boolean isPendingCancel;

    private long pendingSize;

    public Order() {

    }

    public void init(long clientId, CharSequence clientOrderId, long exchangeOrderId, String security, Side side, long size, long price, Type type, TimeInForce tif) {
        this.clientId = clientId;
        this.clientOrderId.setLength(0);
        this.clientOrderId.append(clientOrderId);
        this.side = side;
        this.type = type;
        this.originalSize = this.totalSize = size;
        this.price = price;
        this.executedSize = 0;
        this.security = security;
        this.id = exchangeOrderId;
        this.acceptTime = -1;
        this.restTime = -1;
        this.reduceTime = -1;
        this.executeTime = -1;
        this.cancelTime = -1;
        this.rejectTime = -1;
        this.priceLevel = null;
        this.tif = tif;
        this.isResting = false;
        this.isPendingCancel = false;
        this.pendingSize = -1;
        this.next = this.prev = null;
    }

    public final long getOpenSize() {
        return totalSize - executedSize;
    }

    public final long getCanceledSize() {
        return originalSize - getOpenSize() - executedSize;
    }

    public final boolean isTerminal() {
        return getOpenSize() == 0;
    }

    public final void setPendingCancel() {
        this.isPendingCancel = true;
    }

    public final boolean isPendingCancel() {
        return isPendingCancel;
    }

    public final boolean isResting() {
        return isResting;
    }

    public final Side getSide() {
        return side;
    }

    public final long getId() {
        return id;
    }

    public final long getClientId() {
        return clientId;
    }

    public final CharSequence getClientOrderId() {
        return clientOrderId;
    }

    public final String getSecurity() {
        return security;
    }

    public final Type getType() {
        return type;
    }

    public final TimeInForce getTimeInForce() {
        return tif;
    }

    public final long getPrice() {
        return price;
    }

    public final PriceLevel getPriceLevel() {
        return priceLevel;
    }

    public final void setPriceLevel(PriceLevel priceLevel) {
        this.priceLevel = priceLevel;
    }

    public void reduceTo(long time, long newTotalSize) {

    	if (newTotalSize <= executedSize) {

    		cancel(time, CancelReason.USER);

    		return;
    	}

    	if (newTotalSize > totalSize) {

    		newTotalSize = totalSize;
    	}

    	this.totalSize = newTotalSize;

    	this.reduceTime = time;

    	int x = listeners.size();

    	for(int i = x - 1; i >= 0; i--) {

    		listeners.get(i).onOrderReduced(time, this, this.totalSize);
    	}
    }

    public void cancel(long time, long sizeToCancel) {

    	cancel(time, sizeToCancel, CancelReason.USER);
    }

    public void cancel(long time, long sizeToCancel, CancelReason reason) {

    	if (sizeToCancel >= getOpenSize()) {

    		cancel(time, reason);

    		return;
    	}

    	long newSize = getOpenSize() - sizeToCancel + executedSize;

    	this.totalSize = newSize;

    	this.reduceTime = time;

    	int x = listeners.size();

    	for(int i = x - 1; i >= 0; i--) {

    		listeners.get(i).onOrderReduced(time, this, newSize);
    	}
    }

    public void cancel(long time) {

    	cancel(time, CancelReason.USER);
    }

    public void cancel(long time, CancelReason reason) {

    	this.totalSize = this.executedSize;

    	this.cancelTime = time;

    	int x = listeners.size();

    	for(int i = x - 1; i >= 0; i--) {

    		listeners.get(i).onOrderCanceled(time, this, reason);
    	}

    	for(int i = x - 1; i >= 0; i--) {

    		listeners.get(i).onOrderTerminated(time, this);
    	}

    	listeners.clear();
    }

    public void execute(long time, long sizeToExecute) {

    	execute(time, ExecuteSide.TAKER, sizeToExecute, this.price, -1, -1);
    }

    public void execute(long time, ExecuteSide execSide, long sizeToExecute, long priceExecuted, long executionId, long matchId) {

    	if (sizeToExecute > getOpenSize()) {

    		sizeToExecute = getOpenSize();
    	}

    	this.executedSize += sizeToExecute;

    	this.executeTime = time;

    	int x = listeners.size();

    	for(int i = x - 1; i >= 0; i--) {

    		listeners.get(i).onOrderExecuted(time, this, execSide, sizeToExecute, priceExecuted, executionId, matchId);
    	}

    	if (isTerminal()) {

        	for(int i = x - 1; i >= 0; i--) {

        		listeners.get(i).onOrderTerminated(time, this);
        	}

    		listeners.clear();
    	}
    }

    /**
     * This method of course produces garbage and should be used only for debugging purposes.
     * Use toCharSequence(StringBuilder) instead in order to avoid producing garbage.
     *
     *  @return a newly created String object containing the information about this order instance
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        return toCharSequence(sb).toString();
    }

    /**
     * This method does not produce garbage. Re-use the same StringBuilder over and over again in order to avoid creating garbage.
     *
     * @param sb the StringBuilder where the information will be written to.
     * @return a CharSequence (i.e. the StringBuilder passed) containing the order information
     */
    public CharSequence toCharSequence(StringBuilder sb) {
        sb.append("Order [id=").append(id).append(", clientId=").append(clientId)
            .append(", clientOrderId=").append(clientOrderId).append(", side=")
            .append(side).append(", security=").append(security).append(", originalSize=").append(originalSize)
            .append(", openSize=").append(getOpenSize()).append(", executedSize=").append(executedSize)
            .append(", canceledSize=").append(getCanceledSize());

        if (type != Type.MARKET) {
            sb.append(", price=").append(DoubleUtils.toDouble(price));
        }

        sb.append(", type=").append(type);

        if (type != Type.MARKET) {
            sb.append(", tif=").append(tif);
        }

        sb.append("]");

        return sb;
    }
}
