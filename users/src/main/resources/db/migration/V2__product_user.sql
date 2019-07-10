INSERT INTO public.user_details (id, active, address, email, first_name, last_name, phone_number, secret_hash, user_group)
VALUES ('76057f97-a2a6-44e1-8457-7f5c461df918', true, 'Kalvariuskaia 42, Minsk', 'dpoplavsky@kyriba.com', 'Dmitry', 'Poplavsky', '7788', '5F4DCC3B5AA765D61D8327DEB882CF99', 'PRODUCT');

INSERT INTO public.users (id, user_details_id) VALUES ('6bafbf07-0ac7-44c9-a209-f58609984d50', '76057f97-a2a6-44e1-8457-7f5c461df918');