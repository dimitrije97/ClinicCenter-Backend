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

insert into doctor(id, clinic_id) values
('adfa0bd5-c1b5-41d7-adc4-b6951beb9055', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('8dbea129-360a-4d77-afca-5c5bb94174c1', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a', '7833aba3-bf46-4d25-a856-651d7d7ba279'),
('9608b8fe-4cff-49ee-8997-2ef2494e21cb', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('f130446d-5b2e-4908-a6be-1c0218e15d52', '229c6688-afd8-4137-8d15-92655b1f05ee');

insert into nurse(id, clinic_id) values
('358359e1-9a6d-4196-95a3-af8fdc294fd4', '7833aba3-bf46-4d25-a856-651d7d7ba279'),
('767609d0-dd8a-487f-9ef0-a2433b71d49b', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('b7b8e9ee-44f2-4722-a0c0-5343e1595f1e', '7833aba3-bf46-4d25-a856-651d7d7ba279'),
('eb8381fb-fd8d-4e8f-bd31-ed82626230bd', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('88a23210-81ff-4088-9b1b-1d235016162f', '229c6688-afd8-4137-8d15-92655b1f05ee');

insert into clinic_center_admin(id) values
('d2f71233-b6de-42ba-98ba-5d4befc02efe'),
('ff453f1b-a5fe-49ee-a22a-be037a55a78a');

insert into emergency_room(id, name, number, deleted, clinic_id) values
('3210eb49-a55a-422b-9b9f-0ca04caf65db', 'SalaA', '1001', 'false', 'f82b1f9a-d9ce-49a3-b6b3-7d13e7248a7b'),
('2ccc671f-aa07-4d1c-be28-9d83e11bdddb', 'SalaB', '2001', 'false', '7833aba3-bf46-4d25-a856-651d7d7ba279'),
('5baa8f38-0198-4d85-b725-2a1dc6b266ce', 'SalaC', '3001', 'false', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('4e129247-36f4-48bc-8970-ffbbb5de95ec', 'SalaD', '3002', 'false', '5d26aa42-f1f9-45a5-9e31-db07d5df6c1a'),
('05a4c407-834c-47ed-be62-1ab76c70a629', 'SalaE', '4001', 'true', '229c6688-afd8-4137-8d15-92655b1f05ee');