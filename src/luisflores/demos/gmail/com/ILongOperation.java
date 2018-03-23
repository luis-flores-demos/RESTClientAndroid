package luisflores.demos.gmail.com;

/**
 * Created by Luis FM on 26/02/2018.
 */

public interface ILongOperation<T> {

    public Class<T> getTypeClass();

    public T doInBackground();

    public void onPostExecute(int code, T result);

    public void onPreExecute();

}
