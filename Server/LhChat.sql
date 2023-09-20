PGDMP     %                     {           LhChat    14.4    14.4 /               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                        1262    49686    LhChat    DATABASE     d   CREATE DATABASE "LhChat" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "LhChat";
                postgres    false                        3079    49687 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            !           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            �            1255    49697    appartenenza_creatore()    FUNCTION     �   CREATE FUNCTION public.appartenenza_creatore() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO appartenenza_stanza (username, id_stanza)
        VALUES (NEW.nome_admin, NEW.id_stanza);
    RETURN NEW;
END;
$$;
 .   DROP FUNCTION public.appartenenza_creatore();
       public          postgres    false            �            1255    49698    delete_richiesta_on_insert()    FUNCTION     �   CREATE FUNCTION public.delete_richiesta_on_insert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM richiesta_stanza
    WHERE utente = NEW.username AND id_stanza = NEW.id_stanza;
    RETURN NEW;
END;
$$;
 3   DROP FUNCTION public.delete_richiesta_on_insert();
       public          postgres    false            �            1259    49699    appartenenza_stanza    TABLE     y   CREATE TABLE public.appartenenza_stanza (
    username character varying(32) NOT NULL,
    id_stanza integer NOT NULL
);
 '   DROP TABLE public.appartenenza_stanza;
       public         heap    postgres    false            �            1259    49702 !   appartenenza_stanza_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.appartenenza_stanza_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.appartenenza_stanza_id_stanza_seq;
       public          postgres    false    210            "           0    0 !   appartenenza_stanza_id_stanza_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.appartenenza_stanza_id_stanza_seq OWNED BY public.appartenenza_stanza.id_stanza;
          public          postgres    false    211            �            1259    49703 	   messaggio    TABLE     �   CREATE TABLE public.messaggio (
    mittente character varying(32) NOT NULL,
    id_stanza integer NOT NULL,
    ora_invio timestamp without time zone NOT NULL,
    testo text NOT NULL
);
    DROP TABLE public.messaggio;
       public         heap    postgres    false            �            1259    49708    messaggio_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.messaggio_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.messaggio_id_stanza_seq;
       public          postgres    false    212            #           0    0    messaggio_id_stanza_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.messaggio_id_stanza_seq OWNED BY public.messaggio.id_stanza;
          public          postgres    false    213            �            1259    49709    richiesta_stanza    TABLE     t   CREATE TABLE public.richiesta_stanza (
    utente character varying(32) NOT NULL,
    id_stanza integer NOT NULL
);
 $   DROP TABLE public.richiesta_stanza;
       public         heap    postgres    false            �            1259    49712    richiesta_accesso_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.richiesta_accesso_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.richiesta_accesso_id_stanza_seq;
       public          postgres    false    214            $           0    0    richiesta_accesso_id_stanza_seq    SEQUENCE OWNED BY     b   ALTER SEQUENCE public.richiesta_accesso_id_stanza_seq OWNED BY public.richiesta_stanza.id_stanza;
          public          postgres    false    215            �            1259    49713    stanza    TABLE     �   CREATE TABLE public.stanza (
    id_stanza integer NOT NULL,
    nome_stanza character varying(32) NOT NULL,
    nome_admin character varying(32) NOT NULL
);
    DROP TABLE public.stanza;
       public         heap    postgres    false            �            1259    49716    stanza_id_stanza_seq    SEQUENCE     �   CREATE SEQUENCE public.stanza_id_stanza_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.stanza_id_stanza_seq;
       public          postgres    false    216            %           0    0    stanza_id_stanza_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.stanza_id_stanza_seq OWNED BY public.stanza.id_stanza;
          public          postgres    false    217            �            1259    49717    utente    TABLE     y   CREATE TABLE public.utente (
    username character varying(32) NOT NULL,
    password character varying(32) NOT NULL
);
    DROP TABLE public.utente;
       public         heap    postgres    false            r           2604    49720    appartenenza_stanza id_stanza    DEFAULT     �   ALTER TABLE ONLY public.appartenenza_stanza ALTER COLUMN id_stanza SET DEFAULT nextval('public.appartenenza_stanza_id_stanza_seq'::regclass);
 L   ALTER TABLE public.appartenenza_stanza ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    211    210            s           2604    49721    messaggio id_stanza    DEFAULT     z   ALTER TABLE ONLY public.messaggio ALTER COLUMN id_stanza SET DEFAULT nextval('public.messaggio_id_stanza_seq'::regclass);
 B   ALTER TABLE public.messaggio ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    213    212            t           2604    49722    richiesta_stanza id_stanza    DEFAULT     �   ALTER TABLE ONLY public.richiesta_stanza ALTER COLUMN id_stanza SET DEFAULT nextval('public.richiesta_accesso_id_stanza_seq'::regclass);
 I   ALTER TABLE public.richiesta_stanza ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    215    214            u           2604    49723    stanza id_stanza    DEFAULT     t   ALTER TABLE ONLY public.stanza ALTER COLUMN id_stanza SET DEFAULT nextval('public.stanza_id_stanza_seq'::regclass);
 ?   ALTER TABLE public.stanza ALTER COLUMN id_stanza DROP DEFAULT;
       public          postgres    false    217    216                      0    49699    appartenenza_stanza 
   TABLE DATA           B   COPY public.appartenenza_stanza (username, id_stanza) FROM stdin;
    public          postgres    false    210   }8                 0    49703 	   messaggio 
   TABLE DATA           J   COPY public.messaggio (mittente, id_stanza, ora_invio, testo) FROM stdin;
    public          postgres    false    212   �9                 0    49709    richiesta_stanza 
   TABLE DATA           =   COPY public.richiesta_stanza (utente, id_stanza) FROM stdin;
    public          postgres    false    214   �9                 0    49713    stanza 
   TABLE DATA           D   COPY public.stanza (id_stanza, nome_stanza, nome_admin) FROM stdin;
    public          postgres    false    216   �9                 0    49717    utente 
   TABLE DATA           4   COPY public.utente (username, password) FROM stdin;
    public          postgres    false    218   �:       &           0    0 !   appartenenza_stanza_id_stanza_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.appartenenza_stanza_id_stanza_seq', 1, false);
          public          postgres    false    211            '           0    0    messaggio_id_stanza_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.messaggio_id_stanza_seq', 1, false);
          public          postgres    false    213            (           0    0    richiesta_accesso_id_stanza_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.richiesta_accesso_id_stanza_seq', 1, false);
          public          postgres    false    215            )           0    0    stanza_id_stanza_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.stanza_id_stanza_seq', 15, true);
          public          postgres    false    217            y           2606    49725    stanza stanza_nome_stanza_key 
   CONSTRAINT     _   ALTER TABLE ONLY public.stanza
    ADD CONSTRAINT stanza_nome_stanza_key UNIQUE (nome_stanza);
 G   ALTER TABLE ONLY public.stanza DROP CONSTRAINT stanza_nome_stanza_key;
       public            postgres    false    216            {           2606    49727    stanza stanza_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.stanza
    ADD CONSTRAINT stanza_pkey PRIMARY KEY (id_stanza);
 <   ALTER TABLE ONLY public.stanza DROP CONSTRAINT stanza_pkey;
       public            postgres    false    216            w           2606    49729 !   richiesta_stanza unique_richiesta 
   CONSTRAINT     i   ALTER TABLE ONLY public.richiesta_stanza
    ADD CONSTRAINT unique_richiesta UNIQUE (utente, id_stanza);
 K   ALTER TABLE ONLY public.richiesta_stanza DROP CONSTRAINT unique_richiesta;
       public            postgres    false    214    214            }           2606    49731    utente utente_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (username);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    218            �           2620    49732    stanza app_admin    TRIGGER     u   CREATE TRIGGER app_admin AFTER INSERT ON public.stanza FOR EACH ROW EXECUTE FUNCTION public.appartenenza_creatore();
 )   DROP TRIGGER app_admin ON public.stanza;
       public          postgres    false    219    216            �           2620    49733 ,   appartenenza_stanza delete_richiesta_trigger    TRIGGER     �   CREATE TRIGGER delete_richiesta_trigger AFTER INSERT ON public.appartenenza_stanza FOR EACH ROW EXECUTE FUNCTION public.delete_richiesta_on_insert();
 E   DROP TRIGGER delete_richiesta_trigger ON public.appartenenza_stanza;
       public          postgres    false    210    220            ~           2606    49734 6   appartenenza_stanza appartenenza_stanza_id_stanza_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.appartenenza_stanza
    ADD CONSTRAINT appartenenza_stanza_id_stanza_fkey FOREIGN KEY (id_stanza) REFERENCES public.stanza(id_stanza);
 `   ALTER TABLE ONLY public.appartenenza_stanza DROP CONSTRAINT appartenenza_stanza_id_stanza_fkey;
       public          postgres    false    3195    210    216                       2606    49739 5   appartenenza_stanza appartenenza_stanza_username_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.appartenenza_stanza
    ADD CONSTRAINT appartenenza_stanza_username_fkey FOREIGN KEY (username) REFERENCES public.utente(username);
 _   ALTER TABLE ONLY public.appartenenza_stanza DROP CONSTRAINT appartenenza_stanza_username_fkey;
       public          postgres    false    3197    210    218            �           2606    49744 "   messaggio messaggio_id_stanza_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messaggio
    ADD CONSTRAINT messaggio_id_stanza_fkey FOREIGN KEY (id_stanza) REFERENCES public.stanza(id_stanza);
 L   ALTER TABLE ONLY public.messaggio DROP CONSTRAINT messaggio_id_stanza_fkey;
       public          postgres    false    3195    216    212            �           2606    49749 !   messaggio messaggio_mittente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messaggio
    ADD CONSTRAINT messaggio_mittente_fkey FOREIGN KEY (mittente) REFERENCES public.utente(username);
 K   ALTER TABLE ONLY public.messaggio DROP CONSTRAINT messaggio_mittente_fkey;
       public          postgres    false    3197    218    212            �           2606    49754 1   richiesta_stanza richiesta_accesso_id_stanza_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.richiesta_stanza
    ADD CONSTRAINT richiesta_accesso_id_stanza_fkey FOREIGN KEY (id_stanza) REFERENCES public.stanza(id_stanza);
 [   ALTER TABLE ONLY public.richiesta_stanza DROP CONSTRAINT richiesta_accesso_id_stanza_fkey;
       public          postgres    false    3195    214    216            �           2606    49759 .   richiesta_stanza richiesta_accesso_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.richiesta_stanza
    ADD CONSTRAINT richiesta_accesso_utente_fkey FOREIGN KEY (utente) REFERENCES public.utente(username);
 X   ALTER TABLE ONLY public.richiesta_stanza DROP CONSTRAINT richiesta_accesso_utente_fkey;
       public          postgres    false    214    3197    218            �           2606    49764    stanza stanza_nome_admin_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stanza
    ADD CONSTRAINT stanza_nome_admin_fkey FOREIGN KEY (nome_admin) REFERENCES public.utente(username);
 G   ALTER TABLE ONLY public.stanza DROP CONSTRAINT stanza_nome_admin_fkey;
       public          postgres    false    218    216    3197                 x�mP�n�0=�3�r��l�I�Q�-M��el_?�BmR��R���[�+)◪���JG=~+�w��6���aF�+�U��e�La	�zk�0x�Q�I�@jͧ�c&	�O��,�x&�g"i3��e)��ⶃ��C�G���M�P��M�)V
5�.��������Bq���ԍ.�jC�{*>�ϑ����߻�����i-x;ZO1	<Q���n�rHM}�X�5֗<=筯U�M�a�%L+07N�u`s�h���L�v&sۆ70�e|���bX:9���  �k�J            x������ � �         %   x�K�MLKK�4,�4�*-N-���44����� ��V         �   x�-��N1��٧�	P��_"$$::���w^�y��� O�E�T��F���b�Ϙ����h��YJʕ�dԊ�ɼmi��>2����(�n��<�(�B�e��#��-=`�Y!t���
�Bm�/r+0#�|�'���׈���	�����ߢ.��;�x��Û|{2�M��O��s4)���ٲ`���F'>�5�^Fi��V���ԭ�/r� )�#K�4��#�d�a�         �  x�5��n����|�#l(����B)��:ґ�8���!��?fmm	d)L;��c|FԂ�F ���(b�\I��̥�>��}1�A�ۜ�ގ��d&�k�v��i��6�X�L�J6���m8t�,�%����k������ɼ�R���1L���(��w"x�FG=h�
])R�e%E'�#�ؐi����]#��B�|����9!/�R����^Yu�KL��	�T}F/?��`X%Φ�4N�*�}"���'S���76Z�̷��d��h����f��J��e�]o��%��Qd�3����+����U�����F��h�&�b�êf�W=��9o�������o=/��%O���2�>��x�¾\K��?�U�z��*�`�HY�n��n1���F��h��_m���6A�D8o`E���}�:e;���i����:o;�j8��`��hV��
�R�o߷��-7�'M�D� ��I�v��kd�h%W�Wo��8V0-<��Ӫ�1	�ȭ���I����Y�/7�F;ls!nPQ�Y��D[��Ձ��)���Ɖ4w�&�0=�ǃ��Q<�\s�ޤ�X�Y�/+y˜��=���%��gw��P�u���=�m�\�ȯA�I�\O�Q����;��9��q����>��mm�b&%C��W��5�|jڰ����_�;����b������Adt���W�E8��u*��d_������s%w�� ���2Z�j�%�>w�&��C��893Ԓqy֓�A�����՞'y%T��!	�G��q��,kV���q�WW�T*Tʺ!)L���c�,�7iz" �|�`l�I���.��mF��Q��eOr����y�q�&4oU&�JIb�}�4[�=K�s�Hx�͐��E�q��4�Rf%+�kA�X8�Y��
򾏦��m��Z3/Jɉ����v�dP�#�'�h�L�r:J�e���H�䕁��:�Y��s�O,�`M.l}C�/LҺ��0fTOg�q�46�O
�+�~�E~�w�h�eB3�ai���:nC~B� �u;����%�G�I���dķ�4�{��D�YF�5�4��)����K�^�`��O�t���+���������k��C��Y����76I�A�|��@e~=�F>�@ :�$6�c9Z�t�k�K)��5�.6��[���ꆘQ
�zT=�Y&B�lG�P�x9���t���:AGPT��=�<�/���n���Nk�1�A������1I�ex��X�\�����\ �`���y��k����
:�r��Rn�<� ^ʠ0��p�)��2�ST�GuB쪃�"��rX�ǓU��i��{r��pZ�IFq}��֫�
/����/��7��7雂f�>Ҽ���Ig�h�i<w-�J08Mx��AY��|pᑶ�/���~����a�Љ�����$[$n���4�R ������coЇ\��Y�`�����4�ucl�s�a=���`8�'�Ë�_�_���A+j�s+>�����7��z������#���
���J�`���:Xv_�:�/2l�fM��K΃�%y C�w�#��Bݥ�za� k	;y�����j��ז�!���;�kۆ۔�<L���s��/���~M�I��=Q���s,��2$\���c��b.�[�ЄL^ἐ�8 ����6�¡~�9�a*En�=��]���5���B�Z�K�+Z�@[�lR=�B������m�Bp���A���K#h     