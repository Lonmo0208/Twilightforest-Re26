package twilightforest.test.util.iterators;

import org.junit.jupiter.api.Test;
import twilightforest.util.iterators.ZippedIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZippedIteratorTests {

	private final List<String> animals = List.of("Aardvark", "Baboon", "Capybara", "Dolphin", "Elephant");
	private final List<String> fruits = List.of("Apple", "Banana", "Cantaloupe");
	private final List<String> empty = List.of();
	private final List<String> zipped = List.of("Aardvark", "Apple", "Baboon", "Banana", "Capybara", "Cantaloupe", "Dolphin", "Elephant");

	@Test
	public void unbalanced() {
		// Slightly unbalanced lists, testing tolerance for input size asymmetry

		List<String> result = StreamSupport.stream(ZippedIterator.fromIterables(this.animals, this.fruits).spliterator(), false).toList();

		assertLinesMatch(this.zipped, result);
	}

	@Test
	public void empty() {
		// Critically unbalanced iterators, empty being first, second, or both

		List<String> collected = new ArrayList<>();

		for (String animal : ZippedIterator.fromIterables(this.empty, this.empty)) {
			collected.add(animal);
		}

		assertTrue(collected.isEmpty());

		for (String animal : ZippedIterator.fromIterables(this.animals, this.empty)) {
			collected.add(animal);
		}

		assertLinesMatch(this.animals, collected);

		for (String fruit : ZippedIterator.fromIterables(this.empty, this.fruits)) {
			collected.add(fruit);
		}

		List<String> combined = new ArrayList<>(this.animals);
		combined.addAll(this.fruits);
		assertLinesMatch(combined, collected);
	}
}