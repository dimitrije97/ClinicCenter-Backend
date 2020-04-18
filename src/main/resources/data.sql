insert into clinic (id, address, deleted, description, name) values
('f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b', 'Adresa1', 'false', 'Opis1', 'Ime1'),
('7833aba3-bf46-4d25-a856-651d7d7ba279', 'Adresa2', 'false', 'Opis2', 'Ime2'),
('5d26aa42-f1f9-45a5-9e31-db07d5df6c1a', 'Adresa3', 'false', 'Opis3', 'Ime3'),
('229c6688-afd8-4137-8d15-92655b1f05ee', 'Adresa4', 'true', 'Opis4', 'Ime4');

insert into patient (id, request_type) values
('e14cf27c-cd22-4004-b0f7-c94fcd13aabf', 'APPROVED'),
('170dccaf-cf4d-4e9e-aa4e-1e3498d17a97', 'APPROVED'),
('d1b5fb6a-0bdd-4df1-8fda-aef7560d7828', 'PENDING'),
('7086dad5-bc13-4977-a7ff-a36d58e068cf', 'PENDING'),
('fa9594c4-430f-420e-a7f0-a3055b7b9aa3', 'DENIED');

insert into admin (id, clinic_id) values
('bbce973b-7ef1-4bf3-99f0-1b87771b3075', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('d719558f-2126-48b0-b76a-3e0bb3c10048', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('a82468e6-8913-48dd-b842-4be4e37aeb35', '7833aba3-bf46-4d25-a856-651d7d7ba279'),
('f9fda70a-7761-43f7-9fe6-00046bb12a02', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('4e58f698-f022-4eee-95ab-810b5ffdcd99', '229c6688-afd8-4137-8d15-92655b1f05ee');

insert into examination_type(id, name, price, deleted) values
('4e7079a6-807a-4aec-bf69-177bea1dcce5', 'Ocni', '1000', 'false'),
('e823e955-6001-434a-9fd3-c80e490d0184', 'Stomatoloski', '2000', 'false'),
('b26933ae-484a-4b3f-a15b-1e1796e0a8ad', 'Kardio', '3000', 'false'),
('342265d1-3282-4bb7-b689-cefb42b92818', 'Plucni', '2500', 'false');

insert into doctor(id, clinic_id, examination_type_id, start_at, end_at) values
('adfa0bd5-c1b5-41d7-adc4-b6951beb9055', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b','e823e955-6001-434a-9fd3-c80e490d0184', '08:00:00', '20:00:00'),
('8dbea129-360a-4d77-afca-5c5bb94174c1', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b', 'e823e955-6001-434a-9fd3-c80e490d0184', '15:00:00', '22:00:00'),
('b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a', '7833aba3-bf46-4d25-a856-651d7d7ba279', 'e823e955-6001-434a-9fd3-c80e490d0184', '08:00:00', '20:00:00'),
('9608b8fe-4cff-49ee-8997-2ef2494e21cb', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a', 'b26933ae-484a-4b3f-a15b-1e1796e0a8ad', '01:00:00', '09:00:00'),
('f130446d-5b2e-4908-a6be-1c0218e15d52', '229c6688-afd8-4137-8d15-92655b1f05ee', '342265d1-3282-4bb7-b689-cefb42b92818', '08:00:00', '20:00:00');

insert into nurse(id, clinic_id, start_at, end_at) values
('358359e1-9a6d-4196-95a3-af8fdc294fd4', '7833aba3-bf46-4d25-a856-651d7d7ba279', '08:00:00', '20:00:00'),
('767609d0-dd8a-487f-9ef0-a2433b71d49b', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b', '16:00:00', '22:00:00'),
('b7b8e9ee-44f2-4722-a0c0-5343e1595f1e', '7833aba3-bf46-4d25-a856-651d7d7ba279', '01:00:00', '10:00:00'),
('eb8381fb-fd8d-4e8f-bd31-ed82626230bd', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a', '09:00:00', '18:00:00'),
('88a23210-81ff-4088-9b1b-1d235016162f', '229c6688-afd8-4137-8d15-92655b1f05ee', '16:00:00', '22:00:00');

insert into clinic_center_admin(id, clinic_center_admin_type) values
('d2f71233-b6de-42ba-98ba-5d4befc02efe', 'PREDEF'),
('ff453f1b-a5fe-49ee-a22a-be037a55a78a' ,'REGULAR');

insert into emergency_room(id, name, number, deleted, clinic_id) values
('3210eb49-a55a-422b-9b9f-0ca04caf65db', 'SalaA', '1001', 'false', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('2ccc671f-aa07-4d1c-be28-9d83e11bdddb', 'SalaB', '2001', 'false', '7833aba3-bf46-4d25-a856-651d7d7ba279'),
('5baa8f38-0198-4d85-b725-2a1dc6b266ce', 'SalaC', '3001', 'false', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('4e129247-36f4-48bc-8970-ffbbb5de95ec', 'SalaD', '3002', 'false', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('05a4c407-834c-47ed-be62-1ab76c70a629', 'SalaE', '4001', 'true', '229c6688-afd8-4137-8d15-92655b1f05ee');

insert into schedule(id, approved, date, start_at, end_at, doctor_id, patient_id, reason_of_unavailability, nurse_id) values
('bb17dce6-632c-4eca-ab3e-977d7ffc0780', 'true', '2020/04/01', '08:30:00', '09:30:00', 'adfa0bd5-c1b5-41d7-adc4-b6951beb9055', 'e14cf27c-cd22-4004-b0f7-c94fcd13aabf', 'EXAMINATION', null),
('ca33f7c5-3555-499b-923a-d8bb12fcd358', 'true', '2020/04/02', '16:30:00', '17:30:00', '8dbea129-360a-4d77-afca-5c5bb94174c1', 'e14cf27c-cd22-4004-b0f7-c94fcd13aabf', 'EXAMINATION', null),
('8e7819c1-df81-4b28-96d7-ca3206e45f8f', 'true', '2020/04/01', '10:30:00', '11:30:00', 'adfa0bd5-c1b5-41d7-adc4-b6951beb9055', '170dccaf-cf4d-4e9e-aa4e-1e3498d17a97', 'EXAMINATION', null),
('ca00ff6a-bc8c-46ab-83a5-9d64cb62aa6b', 'true', '2020/04/03', '12:30:00', '13:30:00', 'b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a', '170dccaf-cf4d-4e9e-aa4e-1e3498d17a97', 'EXAMINATION', null),
('6999e7cd-f174-4e74-8048-f192f2052092', 'false', '2020/07/04', '06:20:00', '07:20:00', '9608b8fe-4cff-49ee-8997-2ef2494e21cb', 'e14cf27c-cd22-4004-b0f7-c94fcd13aabf', 'POTENTIAL_EXAMINATION', null),
('922b6438-600b-4e03-b82f-25850e56dbf8', 'true', '2020/04/04', '05:00:00', '06:00:00', '9608b8fe-4cff-49ee-8997-2ef2494e21cb', '170dccaf-cf4d-4e9e-aa4e-1e3498d17a97', 'EXAMINATION', null),
('75d40c59-ba01-4831-930c-13ce06f7e4fe', 'false', '2020/08/04', '04:30:00', '05:30:00', '9608b8fe-4cff-49ee-8997-2ef2494e21cb', null, 'POTENTIAL_EXAMINATION', null),
('092d2cc8-0479-413f-a760-469bccd63057', 'false', '2020/07/04', '07:30:00', '08:30:00', '9608b8fe-4cff-49ee-8997-2ef2494e21cb', 'e14cf27c-cd22-4004-b0f7-c94fcd13aabf', 'POTENTIAL_EXAMINATION', null),
('31cd744d-d915-4b3b-a531-500535903706', 'true', '2020/07/05', null, null, '8dbea129-360a-4d77-afca-5c5bb94174c1', null, 'VACATION', null),
('c9dfb134-6d4d-4a98-b8b3-04b1e38bba88', 'true', '2020/07/06', null, null, '8dbea129-360a-4d77-afca-5c5bb94174c1', null, 'VACATION', null),
('f8a80f04-9d86-4f3d-9572-e8bcf3707cfa', 'false', '2020/07/07', null, null, 'b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a', null, 'POTENTIAL_VACATION', null),
('f17a2996-602a-4433-bdd2-7e8af0f2e969', 'false', '2020/07/08', null, null, 'b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a', null, 'POTENTIAL_VACATION', null),
('5c19aa40-c52f-45e7-9e67-9c1af0c970d3', 'true', '2020/07/09', null, null, null, null, 'VACATION', '358359e1-9a6d-4196-95a3-af8fdc294fd4'),
('b5480154-79c9-4cef-8991-19b9ce38d1d1', 'false', '2020/07/10', null, null, null, null, 'POTENTIAL_VACATION', '358359e1-9a6d-4196-95a3-af8fdc294fd4');

insert into examination(id, status, schedule_id, emergency_room_id) values
('4683148e-9e76-4c78-890a-a15de40287bb', 'APPROVED', 'bb17dce6-632c-4eca-ab3e-977d7ffc0780', '3210eb49-a55a-422b-9b9f-0ca04caf65db'),
('a3102c53-f2b6-4476-aa2a-ee63fc553e4f', 'APPROVED', 'ca33f7c5-3555-499b-923a-d8bb12fcd358', '3210eb49-a55a-422b-9b9f-0ca04caf65db'),
('18a36d59-3fc6-4689-b6c7-f348b04c8e7a', 'APPROVED', '8e7819c1-df81-4b28-96d7-ca3206e45f8f', '3210eb49-a55a-422b-9b9f-0ca04caf65db'),
('6cdea8b6-ea07-44aa-9742-5cf2b7e4badf', 'APPROVED', 'ca00ff6a-bc8c-46ab-83a5-9d64cb62aa6b', '2ccc671f-aa07-4d1c-be28-9d83e11bdddb'),
('7cf8fadb-cd00-445d-9707-7ad907814dc3', 'PENDING', '6999e7cd-f174-4e74-8048-f192f2052092', null),
('93ad6de1-0fe8-4b5d-bb89-488e769a63b1', 'APPROVED', '922b6438-600b-4e03-b82f-25850e56dbf8', '5baa8f38-0198-4d85-b725-2a1dc6b266ce'),
('5e832464-08c7-4f60-8c37-15d3581223b1', 'CONFIRMING', '092d2cc8-0479-413f-a760-469bccd63057', '3210eb49-a55a-422b-9b9f-0ca04caf65db'),
('c1274b05-dd53-4dff-87d9-22f5d62ac5ec', 'CONFIRMING', '75d40c59-ba01-4831-930c-13ce06f7e4fe', '4e129247-36f4-48bc-8970-ffbbb5de95ec');

insert into doctor_patient(doctor_id, patient_id) values
('adfa0bd5-c1b5-41d7-adc4-b6951beb9055', 'e14cf27c-cd22-4004-b0f7-c94fcd13aabf'),
('8dbea129-360a-4d77-afca-5c5bb94174c1', 'e14cf27c-cd22-4004-b0f7-c94fcd13aabf'),
('adfa0bd5-c1b5-41d7-adc4-b6951beb9055', '170dccaf-cf4d-4e9e-aa4e-1e3498d17a97'),
('b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a', '170dccaf-cf4d-4e9e-aa4e-1e3498d17a97'),
('9608b8fe-4cff-49ee-8997-2ef2494e21cb', '170dccaf-cf4d-4e9e-aa4e-1e3498d17a97');

insert into medicine(id, name, deleted) values
('d8572b0c-b38a-4021-b580-f4611789cadb', 'Lek1', 'false'),
('7fda0019-0ec4-41ca-a8e7-676a9ad35d16', 'Lek2', 'false'),
('a3400e51-faf2-43bf-8b90-ee1d14a314ad', 'Lek3', 'true');

insert into diagnosis(id, name, deleted) values
('09609161-8237-4c03-b0bf-6366e873cec8', 'Dijagnoza1', 'false'),
('7a78f767-3cd9-44a2-9176-e1942a903e39', 'Dijagnoza2', 'false'),
('bdfcc5ed-53cd-4d50-af16-65134bd1dd0c', 'Dijagnoza3', 'true');