use DB2Project;

create or replace trigger zeroPointsOnNewUser
before insert on DB2Project.user
for each row
set new.points = 0;

DELIMITER $$
create or replace trigger newReview
before insert on DB2Project.review
for each row
begin
	declare tmp_points bigint;
	declare curr_points bigint;
	set tmp_points = 0;
	if (new.age IS NOT NULL) then 
		set tmp_points = tmp_points + 2;
	end if;
	if (new.sex IS NOT NULL) then 
		set tmp_points = tmp_points + 2;
	end if;
	if (new.level IS NOT NULL) then 
		set tmp_points = tmp_points + 2;
	end if;
	set new.points = tmp_points;
	set curr_points = (select points from DB2Project.user U where U.id = new.user); 
	update DB2Project.user U set points = curr_points + tmp_points where U.id = new.user;
end; $$
DELIMITER ;

DELIMITER $$
create or replace trigger cancelReview
after delete on DB2Project.review
for each row
begin
	declare tmp_points bigint;
	declare curr_points bigint;
	set tmp_points = 0;
	if (old.age IS NOT NULL) then 
		set tmp_points = tmp_points + 2;
	end if;
	if (old.sex IS NOT NULL) then 
		set tmp_points = tmp_points + 2;
	end if;
	if (old.level IS NOT NULL) then 
		set tmp_points = tmp_points + 2;
	end if;
	set curr_points = (select points from DB2Project.user U where U.id = old.user); 
	update DB2Project.user U set points = curr_points - tmp_points where U.id = old.user;
end; $$
DELIMITER ;

DELIMITER $$
create or replace trigger newAnswerForReview
after insert on DB2Project.manswer
for each row
begin
	declare curr_points bigint;
	declare review_user int;
	select points, user into curr_points, review_user from DB2Project.review R where R.id = new.review; 
	update DB2Project.review R set points = curr_points + 1 where R.id = new.review;
	set curr_points = (select points from DB2Project.user U where U.id = review_user); 
	update DB2Project.user U set points = curr_points + 1 where U.id = review_user;
end; $$
DELIMITER ;

DELIMITER $$
create or replace trigger deleteAnswer
after delete on DB2Project.manswer
for each row
begin
	declare curr_points bigint;
	declare review_user int;
	select points, user into curr_points, review_user from DB2Project.review R where R.id = old.review; 
	update DB2Project.review R set points = curr_points - 1 where R.id = old.review;
	set curr_points = (select points from DB2Project.user U where U.id = review_user); 
	update DB2Project.user U set points = curr_points - 1 where U.id = review_user;
end; $$
DELIMITER ;