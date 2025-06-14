create table public.course (
    credits integer,
    code varchar(255),
    course_id varchar(255) not null,
    department_id varchar(255),
    name varchar(255),
    primary key (course_id)
);

create table public.department (
    code varchar(255),
    department_id varchar(255) not null,
    name varchar(255),
    primary key (department_id)
);

create table public.transcript (
    status smallint check (status between 0 and 1),
    encrypted_key_hod text,
    encrypted_key_student text,
    encrypted_key_supervisor text,
    encrypted_transcript_data text,
    hod_digital_signature text,
    hod_id varchar(255),
    student_id varchar(255),
    transcript_id varchar(255) not null,
    primary key (transcript_id)
);

create table public.transcript_access (
    status smallint check (status between 0 and 3),
    inquiry_id varchar(255) not null,
    requestee_id varchar(255),
    requester_id varchar(255),
    transcript_id varchar(255),
    participants jsonb,
    primary key (inquiry_id)
);

create table public.user (
    department_id varchar(255),
    email varchar(255),
    full_name varchar(255),
    password varchar(255),
    public_key text,
    role varchar(255) check (
        role in (
            'STUDENT',
            'SUPERVISOR',
            'HOD'
        )
    ),
    supervisor_id varchar(255),
    user_id varchar(255) not null,
    primary key (user_id)
);

alter table if exists public.transcript
add constraint FKdoifppuqhoca0ges8d4infafo foreign key (hod_id) references public.user;

alter table if exists public.transcript
add constraint FKr60e7lofp662sy54aa1uq0duo foreign key (student_id) references public.user;

alter table if exists public.transcript_access
add constraint FKpgkqu770agxnol4ynb89a3wah foreign key (requestee_id) references public.user;

alter table if exists public.transcript_access
add constraint FKrq8isolyn8nbvxeatyaty6xed foreign key (requester_id) references public.user;

alter table if exists public.transcript_access
add constraint FKs61honakk0hhmi4tayxq1s3ov foreign key (transcript_id) references public.transcript;

alter table if exists public.user
add constraint FKgkh2fko1e4ydv1y6vtrwdc6my foreign key (department_id) references public.department;