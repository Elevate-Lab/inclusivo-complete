// to filter method
export const toFilter = (d) => {
    let date = '';
    if (d[0]==='a'){
        date = '1'+ d.slice(1)
        return date
    }
    else return d;
}