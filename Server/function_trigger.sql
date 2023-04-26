CREATE OR REPLACE FUNCTION appartenenza_creatore () 
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO appartenenza_stanza (username, id_stanza)
        VALUES (NEW.nome_admin, NEW.id_stanza);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER app_admin
AFTER INSERT ON stanza
FOR EACH ROW
EXECUTE PROCEDURE appartenenza_creatore();