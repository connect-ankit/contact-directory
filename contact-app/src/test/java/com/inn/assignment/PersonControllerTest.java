package com.inn.assignment; 

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inn.assignment.task2.appconfiguration.AppRunner;
import com.inn.assignment.task2.controller.IPersonController;
import com.inn.assignment.task2.core.Constant;
import com.inn.assignment.task2.model.Contact;
import com.inn.assignment.task2.model.Person;

@SpringBootTest(classes = AppRunner.class)
@ExtendWith(SpringExtension.class)
class PersonControllerTest {

    @MockBean
    private IPersonController personController;

    @Test
    void testCreatePerson() {
        Person personToCreate = new Person();
        Contact contact = new Contact();
        contact.setEmailAddress("a@gmail.com");
        contact.setPhoneNumber("8989898989");
        personToCreate.setContact(contact);
        personToCreate.setName("Ankit");
        Mockito.when(personController.create(Mockito.any(Person.class))).thenReturn(personToCreate);
        Person createdPerson = personController.create(personToCreate);
        Assertions.assertNotNull(createdPerson.getName());
      
    }
   
    @Test
    void testDeletePerson() {
        Integer personIdToDelete = 1;
        Mockito.when(personController.delete(Mockito.anyInt())).thenReturn(Constant.ResponseStatus.SUCCESS_JSON);
        String deleteResult = personController.delete(personIdToDelete);
        Assertions.assertEquals(deleteResult, Constant.ResponseStatus.SUCCESS_JSON);
       
    }

}
