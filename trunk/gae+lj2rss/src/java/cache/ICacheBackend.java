package cache;

public interface ICacheBackend<K, V> {

    V get(K key);

}
