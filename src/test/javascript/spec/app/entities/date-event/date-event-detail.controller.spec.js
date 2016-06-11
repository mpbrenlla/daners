'use strict';

describe('Controller Tests', function() {

    describe('DateEvent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDateEvent, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDateEvent = jasmine.createSpy('MockDateEvent');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DateEvent': MockDateEvent,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("DateEventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'danersApp:dateEventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
