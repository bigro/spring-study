INSERT INTO digit VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9);

INSERT INTO temp ( num )
SELECT
    d1.num + d2.num*10 + d3.num*100 + d4.num*1000 + d5.num*10000 + d6.num*100000 as num
FROM
    digit d1, digit d2, digit d3, digit d4, digit d5, digit d6
ORDER BY
    num;

INSERT INTO people ( first_name, last_name )
SELECT CONCAT('first+', num), CONCAT('last+', num) FROM temp;