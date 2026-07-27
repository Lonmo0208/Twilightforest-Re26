package twilightforest.test.util.multiparts;

import net.minecraft.world.entity.Entity;
import org.junit.jupiter.api.Test;
import twilightforest.entity.TFPart;
import twilightforest.util.TFEntityExtensions;
import twilightforest.util.multiparts.MultipartEntityIteratorWrapper;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class MultipartEntityIteratorWrapperTests {

	private Entity mockEntity(TFPart<?>... parts) {
		Entity entity = mock(Entity.class, withSettings().extraInterfaces(TFEntityExtensions.class));
		TFEntityExtensions ext = (TFEntityExtensions) entity;
		when(ext.isMultipartEntity()).thenReturn(parts.length > 0);
		when(ext.getParts()).thenReturn(parts);
		return entity;
	}

	@SuppressWarnings("unchecked")
	private MultipartEntityIteratorWrapper createWrapper(Iterator<Entity> iterator) throws Exception {
		Constructor<MultipartEntityIteratorWrapper> constructor = MultipartEntityIteratorWrapper.class.getDeclaredConstructor(Iterator.class);
		constructor.setAccessible(true);
		return constructor.newInstance(iterator);
	}

	@Test
	public void noPartEntities() throws Exception {
		Iterator<Entity> result = createWrapper(List.of(
			mockEntity(),
			mockEntity(),
			mockEntity()
		).iterator());

		assertNotNull(result);

		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());

		assertFalse(result.hasNext());
	}

	@Test
	public void noTFPartEntities() throws Exception {
		Iterator<Entity> result = createWrapper(List.of(
			mockEntity(),
			mockEntity(mock(TFPart.class)),
			mockEntity(mock(TFPart.class)),
			mockEntity(),
			mockEntity(mock(TFPart.class), mock(TFPart.class)),
			mockEntity()
		).iterator());

		assertNotNull(result);

		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());

		assertFalse(result.hasNext());
	}

	@Test
	public void withTFPartEntities() throws Exception {
		Iterator<Entity> result = createWrapper(List.of(
			mockEntity(),
			mockEntity(mock(TFPart.class)),
			mockEntity(mock(TFPart.class)),
			mockEntity(),
			mockEntity(mock(TFPart.class), mock(TFPart.class)),
			mockEntity()
		).iterator());

		assertNotNull(result);

		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());

		assertFalse(result.hasNext());
	}

}