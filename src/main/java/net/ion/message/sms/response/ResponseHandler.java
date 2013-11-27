package net.ion.message.sms.response;

import net.ion.radon.aclient.Response;

public interface ResponseHandler<T> {

    public T onSuccess(Response response);
    public T onFail(Response response);
    public void onThrow(Throwable t);

}
