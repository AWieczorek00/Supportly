package supportly.supportlybackend.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import supportly.supportlybackend.Model.Part;
import supportly.supportlybackend.Repository.PartRepository;
import supportly.supportlybackend.Service.PartService;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartService partService;

    private List<Part> oldPartList;
    private List<Part> newPartList;

    @BeforeEach
    void setUp() {
        oldPartList = Arrays.asList(
                new Part(1L, "Old Part 1", 100.0f, 10.0f, 5),
                new Part(2L, "Old Part 2", 200.0f, 20.0f, 10)
        );

        newPartList = Arrays.asList(
                new Part(3L, "New Part 1", 150.0f, 15.0f, 7),
                new Part(4L, "New Part 2", 250.0f, 25.0f, 12)
        );
    }

    @Test
    void updatePartList_ShouldDeleteOldPartsAndSaveNewParts() {
        // Given
        doNothing().when(partRepository).deleteAll(oldPartList);
        when(partRepository.saveAll(newPartList)).thenReturn(newPartList);

        // When
        List<Part> result = partService.updatePartList(newPartList, oldPartList);

        // Then
        verify(partRepository, times(1)).deleteAll(oldPartList);
        verify(partRepository, times(1)).saveAll(newPartList);
        assertEquals(newPartList, result);
    }
}
