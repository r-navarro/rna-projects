angular.module('demo-hockey').directive('pagination', function(){
    return {
        scope : {pagination: '@'},
        template: generatePaginationDom
    }

    function generatePaginationDom(scope, elem, attributes){
        console.log(attributes);
        console.log(scope);
        var dom = '<nav><ul class="pagination"><li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
        for(i = 1 ; i <= attributes.pagination ; i++){
            if(attributes.current == i){
                dom += '<li class="active">';
            }else{
                dom += '<li class="inactive">';
            }
            dom += '<a href="#">' + i +' <span class="sr-only">(current)</span></a></li>';
        }
        dom += '<li class="enabled"><a href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li></ul></nav>';

        return dom;
    };
});