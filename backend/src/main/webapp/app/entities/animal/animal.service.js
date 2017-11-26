(function() {
    'use strict';
    angular
        .module('romaninisistemasApp')
        .factory('Animal', Animal);

    Animal.$inject = ['$resource'];

    function Animal ($resource) {
        var resourceUrl =  'api/animals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
