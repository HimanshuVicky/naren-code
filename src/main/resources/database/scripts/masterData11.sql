--liquibase formatted sql
--changeset {authorName}:{id}

delete from citymaster where City in('Agra','Ahmadnagar','Allahabad','Almora','Bareilly','Dalhousie','Dehradun','Faizabad','Jhansi','Kanpur','Lucknow','Mathura','Meerut','Nainital','Pune','Roorkee','Shahjahanpur','Varanasi' )
 and Urban_Status='C.B.';