import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.libin.dao.DaoOwner;
import org.libin.dao.models.Owner;
import org.libin.service.OwnerService;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class DaoOwnerTest {

    private DaoOwner daoOwner ;
    private OwnerService ownerService ;

    private LocalDate bd1 = LocalDate.of(1990, 5, 20);
    private LocalDate bd2 = LocalDate.of(2010, 8, 24);


    @BeforeEach
    void setUp() {
        daoOwner = mock(DaoOwner.class);
        ownerService = new OwnerService(daoOwner);
    }
    @Test
    void getOwner_existingOwner(){
        Owner owner = new Owner("User1",bd1);
        owner.setOwnerId(1L);
        when(daoOwner.findOwnerById(1L)).thenReturn(owner);
        Owner result = ownerService.findOwner(1L);

        assertNotNull(result, "not null is required");
        assertEquals("User1", result.getOwnerName());
        verify(daoOwner).findOwnerById(1L);
    }

    @Test
    void saveOwner_invokesDaoSaveOwner() {
        Owner owner = new Owner("NewUser", bd2);
        ownerService.saveOwner(owner);
        verify(daoOwner, times(1)).saveOwner(owner);
    }

    @Test
    void updateOwner_invokesDaoUpdateOwner() {
        Owner owner = new Owner("ExistingUser", bd1);
        owner.setOwnerId(42L);
        ownerService.updateOwner(owner);
        verify(daoOwner, times(1)).updateOwner(owner);
    }

    @Test
    void deleteOwner_invokesDaoDeleteOwner() {
        Owner owner = new Owner("ToDelete", bd2);
        owner.setOwnerId(100L);
        ownerService.deleteOwner(owner);
        verify(daoOwner, times(1)).deleteOwner(owner);
    }

    @Test
    void findOwner_notFound_returnsNull() {
        when(daoOwner.findOwnerById(999L)).thenReturn(null);
        Owner result = ownerService.findOwner(999L);
        assertNull(result, "null required");
        verify(daoOwner).findOwnerById(999L);
    }
}

