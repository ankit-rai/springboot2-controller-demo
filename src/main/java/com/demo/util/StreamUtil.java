package com.demo.util;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Provides static helper methods to handle {@link Stream}s like
 * creating {@code Stream} objects from {@link Iterable} or
 * {@link Iterator} instances.
 *
 * @author Niranjan Nanda
 */
public final class StreamUtil {
	private StreamUtil() {}
	
	/**
	 * Creates a {@link Stream} from an {@link Iterable} instance.
	 *
	 * @param iterable	The input which will be converted to a stream.
	 * @param parallel	If the output is needs to be a parallel stream.
	 * @param <T> The type of elements in the {@code Iterable}.
	 * @return	{@code Stream} object created from {@code Iterable}. An empty
	 * stream if the iterable is null.
	 */
	public static <T> Stream<T> asStream(final Iterable<T> iterable, final boolean parallel) {
		if (iterable == null) {
			return Stream.empty();
		}
		return StreamSupport.stream(iterable.spliterator(), parallel);
	}

	/**
	 * Creates a {@link Stream} from an {@link Iterator} instance.
	 *
	 * @param iterator	The input which will be converted to a stream.
	 * @param parallel	If the output is needs to be a parallel stream.
	 * @param <T> The type of elements in the {@code Iterator}.
	 * @return	{@code Stream} object created from {@code Iterator}. An empty
	 * stream if the iterator is null.
	 */
	public static <T> Stream<T> asStream(final Iterator<T> iterator, final boolean parallel) {
		if (iterator == null) {
			return Stream.empty();
		}

		final Iterable<T> iterable = () -> iterator;
		return StreamSupport.stream(iterable.spliterator(), parallel);
	}
	
	/**
	 * Creates a {@link Stream} from an array.
	 * 
	 * @param array	The array for which a stream will be created.
	 * @param parallel	If the output is needs to be a parallel stream.
	 * @param <T> The type of elements in the array.
	 * @return	{@code Stream} object created from the given array. An empty
	 * stream if the iterator is null.
	 */
	public static <T> Stream<T> asStream(final T[] array, final boolean parallel) {
		if (ArrayUtils.isEmpty(array)) {
			return Stream.empty();
		}
		
		final Stream<T> stream = Stream.of(array); 
		
		if (parallel) {
			return stream.parallel();
		} 
		
		return stream;
	}
	
	/**
	 * Creates a {@link Stream} from a single item.
	 * 
	 * @param singleItem		The item for which a stream will be created.
	 * @param <T> The type of element.
	 * @return	{@code Stream} object created from the given item. An empty
	 * stream if the item is null.
	 */
	public static <T> Stream<T> asStream(final T singleItem) {
		if (singleItem == null) {
			return Stream.empty();
		}
		
		return Stream.of(singleItem);
	}
	
	/**
	 * <p>
	 * Creates a {@link Stream} of {@link Map.Entry} objects for the given map.
	 * If the map is null or empty, it returns an empty stream.
	 * 
	 * @param map	The input map.
	 * @param isParallel		If the output is needs to be a parallel stream.
	 * @param <K> Key type of the map
	 * @param <V> Value type of the map 
	 * @return	A stream of {@code Map.Entry} objects.
	 */
	public static <K, V> Stream<Map.Entry<K, V>> asStreamOfEntries(final Map<K, V> map, final boolean isParallel) {
		Stream<Map.Entry<K, V>> streamOfEntrySet = Stream.empty();
		if (MapUtils.isNotEmpty(map)) {
			if (isParallel) {
				streamOfEntrySet = map.entrySet().parallelStream();
			} else {
				streamOfEntrySet = map.entrySet().stream();
			}
		}
		
		return streamOfEntrySet;
	}
	
	/**
	 * <p>
	 * Creates a {@link Stream} of keys for the given map.
	 * If the map is null or empty, it returns an empty stream.
	 * 
	 * @param map	The input map.
	 * @param isParallel		If the output is needs to be a parallel stream.
	 * @param <K> Key type of the map
	 * @param <V> Value type of the map 
	 * @return	A stream of keys.
	 */
	public static <K, V> Stream<K> asStreamOfKeys(final Map<K, V> map, final boolean isParallel) {
		Stream<K> streamOfKeySet = Stream.empty();
		if (MapUtils.isNotEmpty(map)) {
			if (isParallel) {
				streamOfKeySet = map.keySet().parallelStream();
			} else {
				streamOfKeySet = map.keySet().stream();
			}
		}
		
		return streamOfKeySet;
	}
	
	/**
	 * <p>
	 * Creates a {@link Stream} of values for the given map.
	 * If the map is null or empty, it returns an empty stream.
	 * 
	 * @param map	The input map.
	 * @param isParallel		If the output is needs to be a parallel stream.
	 * @param <K> Key type of the map
	 * @param <V> Value type of the map 
	 * @return	A stream of values.
	 */
	public static <K, V> Stream<V> asStreamOfValues(final Map<K, V> map, final boolean isParallel) {
		Stream<V> streamOfKeySet = Stream.empty();
		if (MapUtils.isNotEmpty(map)) {
			if (isParallel) {
				streamOfKeySet = map.values().parallelStream();
			} else {
				streamOfKeySet = map.values().stream();
			}
		}
		
		return streamOfKeySet;
	}
}
