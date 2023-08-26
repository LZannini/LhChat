PGDMP         '                {           LhChat    15.4    15.3 1    +           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            ,           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            -           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            .           1262    16473    LhChat    DATABASE     {   CREATE DATABASE "LhChat" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "LhChat";
                postgres    false                        3079    16474 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            /           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            �            1255    16484    appartenenza_creatore()    FUNCTION     �   CREATE FUNCTION public.appartenenza_creatore() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO appartenenza_stanza (username, id_stanza)
        VALUES (NEW.nome_admin, NEW.id_stanza);
    RETURN NEW;
END;
$$;
 .   DROP FUNCTION public.appartenenza_creatore();
       public          postgres    false            �            1255    16485    delete_richiesta_on_insert()    FUNCTION     �   CREATE FUNCTION public.delete_richiesta_on_insert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM richiesta_stanza
    WHERE utente = NEW.username AND id_stanza = NEW.id_stanza;
    RETURN NEW;
END;
$$;
 3   DROP FUNCTION public.delete_richiesta_on_insert();
       public          postgres    false            �            1255    16486    update_username_trigger()    FUNCTION     �   CREATE FUNCTION public.update_username_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  UPDATE appartenenza_stanza
  SET username = NEW.username
  WHERE username = OLD.username;
  RETURN NEW;
END;
$$;
 0   DROP FUNCTION public.update_username_trigger();
       public          postgres    false            �            1259    16487    appartenenza_stanza    TABLE     y   CREATE TABLE public.appartenenza_stanza (
    username character varying(32) NOT NULL,
    id_stanza integer NOT NULL
);
 '   DROP TABLE public.appartenenza_stanza;
       public         heap    postgres    false            �            1259    16490 !   appartenenza_stanza_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.appartenenza_stanza_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.appartenenza_stanza_id_stanza_seq;
       public          postgres    false    215            0           0    0 !   appartenenza_stanza_id_stanza_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.appartenenza_stanza_id_stanza_seq OWNED BY public.appartenenza_stanza.id_stanza;
          public          postgres    false    216            �            1259    16491 	   messaggio    TABLE     �   CREATE TABLE public.messaggio (
    mittente character varying(32) NOT NULL,
    id_stanza integer NOT NULL,
    ora_invio timestamp without time zone NOT NULL,
    testo text NOT NULL
);
    DROP TABLE public.messaggio;
       public         heap    postgres    false            �            1259    16496    messaggio_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.messaggio_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.messaggio_id_stanza_seq;
       public          postgres    false    217            1           0    0    messaggio_id_stanza_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.messaggio_id_stanza_seq OWNED BY public.messaggio.id_stanza;
          public          postgres    false    218            �            1259    16497    richiesta_stanza    TABLE     t   CREATE TABLE public.richiesta_stanza (
    utente character varying(32) NOT NULL,
    id_stanza integer NOT NULL
);
 $   DROP TABLE public.richiesta_stanza;
       public         heap    postgres    false            �            1259    16500    richiesta_accesso_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.richiesta_accesso_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.richiesta_accesso_id_stanza_seq;
       public          postgres    false    219            2           0    0    richiesta_accesso_id_stanza_seq    SEQUENCE OWNED BY     b   ALTER SEQUENCE public.richiesta_accesso_id_stanza_seq OWNED BY public.richiesta_stanza.id_stanza;
          public          postgres    false    220            �            1259    16501    stanza    TABLE     �   CREATE TABLE public.stanza (
    id_stanza integer NOT NULL,
    nome_stanza character varying(32) NOT NULL,
    nome_admin character varying(32) NOT NULL
);
    DROP TABLE public.stanza;
       public         heap    postgres    false            �            1259    16504    stanza_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.stanza_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.stanza_id_stanza_seq;
       public          postgres    false    221            3           0    0    stanza_id_stanza_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.stanza_id_stanza_seq OWNED BY public.stanza.id_stanza;
          public          postgres    false    222            �            1259    16505    utente    TABLE     y   CREATE TABLE public.utente (
    username character varying(32) NOT NULL,
    password character varying(32) NOT NULL
);
    DROP TABLE public.utente;
       public         heap    postgres    false            |           2604    16508    appartenenza_stanza id_stanza    DEFAULT     �   ALTER TABLE ONLY public.appartenenza_stanza ALTER COLUMN id_stanza SET DEFAULT nextval('public.appartenenza_stanza_id_stanza_seq'::regclass);
 L   ALTER TABLE public.appartenenza_stanza ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    216    215            }           2604    16509    messaggio id_stanza    DEFAULT     z   ALTER TABLE ONLY public.messaggio ALTER COLUMN id_stanza SET DEFAULT nextval('public.messaggio_id_stanza_seq'::regclass);
 B   ALTER TABLE public.messaggio ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    218    217            ~           2604    16510    richiesta_stanza id_stanza    DEFAULT     �   ALTER TABLE ONLY public.richiesta_stanza ALTER COLUMN id_stanza SET DEFAULT nextval('public.richiesta_accesso_id_stanza_seq'::regclass);
 I   ALTER TABLE public.richiesta_stanza ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    220    219                       2604    16511    stanza id_stanza    DEFAULT     t   ALTER TABLE ONLY public.stanza ALTER COLUMN id_stanza SET DEFAULT nextval('public.stanza_id_stanza_seq'::regclass);
 ?   ALTER TABLE public.stanza ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    222    221                       0    16487    appartenenza_stanza 
   TABLE DATA           B   COPY public.appartenenza_stanza (username, id_stanza) FROM stdin;
    public          postgres    false    215   �;       "          0    16491 	   messaggio 
   TABLE DATA           J   COPY public.messaggio (mittente, id_stanza, ora_invio, testo) FROM stdin;
    public          postgres    false    217   (<       $          0    16497    richiesta_stanza 
   TABLE DATA           =   COPY public.richiesta_stanza (utente, id_stanza) FROM stdin;
    public          postgres    false    219   E<       &          0    16501    stanza 
   TABLE DATA           D   COPY public.stanza (id_stanza, nome_stanza, nome_admin) FROM stdin;
    public          postgres    false    221   z<       (          0    16505    utente 
   TABLE DATA           4   COPY public.utente (username, password) FROM stdin;
    public          postgres    false    223   =       4           0    0 !   appartenenza_stanza_id_stanza_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.appartenenza_stanza_id_stanza_seq', 1, false);
          public          postgres    false    216            5           0    0    messaggio_id_stanza_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.messaggio_id_stanza_seq', 1, false);
          public          postgres    false    218            6           0    0    richiesta_accesso_id_stanza_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.richiesta_accesso_id_stanza_seq', 1, false);
          public          postgres    false    220            7           0    0    stanza_id_stanza_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.stanza_id_stanza_seq', 15, true);
          public          postgres    false    222            �           2606    16513    stanza stanza_nome_stanza_key 
   CONSTRAINT     _   ALTER TABLE ONLY public.stanza
    ADD CONSTRAINT stanza_nome_stanza_key UNIQUE (nome_stanza);
 G   ALTER TABLE ONLY public.stanza DROP CONSTRAINT stanza_nome_stanza_key;
       public            postgres    false    221            �           2606    16515    stanza stanza_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.stanza
    ADD CONSTRAINT stanza_pkey PRIMARY KEY (id_stanza);
 <   ALTER TABLE ONLY public.stanza DROP CONSTRAINT stanza_pkey;
       public            postgres    false    221            �           2606    16557 !   richiesta_stanza unique_richiesta 
   CONSTRAINT     i   ALTER TABLE ONLY public.richiesta_stanza
    ADD CONSTRAINT unique_richiesta UNIQUE (utente, id_stanza);
 K   ALTER TABLE ONLY public.richiesta_stanza DROP CONSTRAINT unique_richiesta;
       public            postgres    false    219    219            �           2606    16517    utente utente_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (username);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    223            �           2620    16518    stanza app_admin    TRIGGER     u   CREATE TRIGGER app_admin AFTER INSERT ON public.stanza FOR EACH ROW EXECUTE FUNCTION public.appartenenza_creatore();
 )   DROP TRIGGER app_admin ON public.stanza;
       public          postgres    false    224    221            �           2620    16519 ,   appartenenza_stanza delete_richiesta_trigger    TRIGGER     �   CREATE TRIGGER delete_richiesta_trigger AFTER INSERT ON public.appartenenza_stanza FOR EACH ROW EXECUTE FUNCTION public.delete_richiesta_on_insert();
 E   DROP TRIGGER delete_richiesta_trigger ON public.appartenenza_stanza;
       public          postgres    false    225    215            �           2620    16520    utente sync_username_trigger    TRIGGER     �   CREATE TRIGGER sync_username_trigger AFTER UPDATE ON public.utente FOR EACH ROW EXECUTE FUNCTION public.update_username_trigger();
 5   DROP TRIGGER sync_username_trigger ON public.utente;
       public          postgres    false    223    226            �           2606    16521 6   appartenenza_stanza appartenenza_stanza_id_stanza_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.appartenenza_stanza
    ADD CONSTRAINT appartenenza_stanza_id_stanza_fkey FOREIGN KEY (id_stanza) REFERENCES public.stanza(id_stanza);
 `   ALTER TABLE ONLY public.appartenenza_stanza DROP CONSTRAINT appartenenza_stanza_id_stanza_fkey;
       public          postgres    false    3205    215    221            �           2606    16526 5   appartenenza_stanza appartenenza_stanza_username_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.appartenenza_stanza
    ADD CONSTRAINT appartenenza_stanza_username_fkey FOREIGN KEY (username) REFERENCES public.utente(username);
 _   ALTER TABLE ONLY public.appartenenza_stanza DROP CONSTRAINT appartenenza_stanza_username_fkey;
       public          postgres    false    3207    215    223            �           2606    16531 "   messaggio messaggio_id_stanza_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messaggio
    ADD CONSTRAINT messaggio_id_stanza_fkey FOREIGN KEY (id_stanza) REFERENCES public.stanza(id_stanza);
 L   ALTER TABLE ONLY public.messaggio DROP CONSTRAINT messaggio_id_stanza_fkey;
       public          postgres    false    221    217    3205            �           2606    16536 !   messaggio messaggio_mittente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messaggio
    ADD CONSTRAINT messaggio_mittente_fkey FOREIGN KEY (mittente) REFERENCES public.utente(username);
 K   ALTER TABLE ONLY public.messaggio DROP CONSTRAINT messaggio_mittente_fkey;
       public          postgres    false    223    3207    217            �           2606    16541 1   richiesta_stanza richiesta_accesso_id_stanza_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.richiesta_stanza
    ADD CONSTRAINT richiesta_accesso_id_stanza_fkey FOREIGN KEY (id_stanza) REFERENCES public.stanza(id_stanza);
 [   ALTER TABLE ONLY public.richiesta_stanza DROP CONSTRAINT richiesta_accesso_id_stanza_fkey;
       public          postgres    false    219    221    3205            �           2606    16546 .   richiesta_stanza richiesta_accesso_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.richiesta_stanza
    ADD CONSTRAINT richiesta_accesso_utente_fkey FOREIGN KEY (utente) REFERENCES public.utente(username);
 X   ALTER TABLE ONLY public.richiesta_stanza DROP CONSTRAINT richiesta_accesso_utente_fkey;
       public          postgres    false    3207    219    223            �           2606    16551    stanza stanza_nome_admin_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stanza
    ADD CONSTRAINT stanza_nome_admin_fkey FOREIGN KEY (nome_admin) REFERENCES public.utente(username);
 G   ALTER TABLE ONLY public.stanza DROP CONSTRAINT stanza_nome_admin_fkey;
       public          postgres    false    223    3207    221                �   x�=��� @�ۏ1vu���fð-��_oć^�i��c��e]h���er3}����<&�f���"�
����XL>x�9�¥�+:�i�CUt��H�	*�t�����Ll1Gҷ���Tx��"� �=4�      "      x������ � �      $   %   x�K�MLKK�4,�4�*-N-���44����� ��V      &   �   x�-�I�0�=������.���'��`��1J�Tꅡ�J�1XYȓț�j��sZLj�-���S�����Q����(� h���7�t���o�E�@�bƿ�	1�eUۀ�����Ona�d�s�����y�X���HD?\�>�      (     x�5�[���F���1�L���U�����v��!���O\��EU���˘Q	fkу H�"	r%�4e6& ��͟���Ԥ���:/�K9F+X����L/�d�i��<f*U��0ѫ~�ګ@.�4n��,���G���4sR���Տ��� �Ե"cn����O�6�}��������o�$T�t;���莚�#U?�SJ<<���׃�H��(뢋���O����5q$KU�O�.�JI焕1|'���4qͧb|����b��LQgB���N��߂�E��R��Bp�nc?���W�Q�]<X�q2�ﱨ�uE�$l����� ��U��3�{��i����kg�ߗ-`a�՗y��lW�(�c|�}4[��v��V4<��U��k�J$�+aI�It.k�2���uu����5D�L�TgwL�ˈ���2�����k����2V����8\����Y&�Z�F���
mە�eL��lC�<��2����u8|H;�c�
�J�<�N��M&�R���'��k+�Ԛ�x��2x����aS�m󐢃l�g=�-Q������oل?���b�=��t���b�$�%�D��v�@d��P��q�
_ק��`��J&h*�+c҇�sy���a�w�5+����+'mu���զ$#	]�a���̟L�Z���edߗ�:L:ԩ%��� �d,�b�97.��!��c6��\jɸ|x�$a�lB>9~7ױE^�1)I�����/�y�U�E8|����B�JYU��x�~�d�|Ȳ#��y�/�6�~U��)xL�\����H
�^U����X���H�b���&��a��ʱ�X+��ס��b�0�ǭ�SAr y��<�gt��e�11) ��`��jCԚ9�KN,�_F����:aD�.��� N�J���o_�������܏3�9b�'z�`|c�2������
F���t�o��~[��a����۳�H�$�X��/C�?�٨����������dG̙�9�G큇��ȲΑƢK���)ƞ�w\��/��X���y���i�=�wt������H�V�F�	�-y&��]�'�#�A��/��b����7 �A��s>Xp?����	S�,��7��*N˿��6�ՠx�)�D��3-�C5��GxǨͤ��
:������ܠ�	��CX?�{/Yn�G����՝[��?����X�o[:��Ŧk3C�-g�h K%f�m�+��o*��^ȭ
sy|`���x�RY9��#���+#��zPE���ڧ-|3R��h�L8��߾aoo�.�?� ��)>�8�Q��D��1�E��,�C�:�	����瘾���@S���i��oP�9�i��jү{y�|���!m�Q	݅��`�Б2����(�Gv��4�?%/��Ʒ��סD���o�
z���*Qץi}uJ5�?n#����x�!��YI?�^�Bk�5�ۊ]�����~�%��.�Z��3�`TJ�����[
�*ꨅ�q�}���<��*V�h���3,�2=�\2Uc�D��[K���$��n��?��?����     