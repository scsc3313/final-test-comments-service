# 데이터설계

#### userinfo table

- user_id
- user_password
- user_name
- user_des
- user_profile_image



#### comment table

- comment_id
- comment_content
- comment_date
- comment_like
- comment_dislike
- user_id

#### eval table

- id (PK)
- comment_id (FK)
- user_id (FK)
- like (bool)