create or replace function delete_obtained_message() returns trigger as $del_obt_msg$
begin
    delete from message where obtained = true;
    return NEW;
end;
$del_obt_msg$ language plpgsql;; /*keep the ;; for testing purpose*/

create or replace trigger delete_obtained_message after update on
    message for each row execute function delete_obtained_message();