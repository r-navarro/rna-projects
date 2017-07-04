package com.carmanagement.services.impls

import com.carmanagement.dto.StatAverageDTO
import com.carmanagement.dto.StatDTO
import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.FullTanksService
import groovy.time.TimeCategory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.math.RoundingMode

@Service
class FullTanksServiceImpl implements FullTanksService {

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    @Override
    FullTank getByVehicleIdAndId(String vehicleId, String fullTankId) throws TechnicalException {
        def fullTank = fullTankRepository.findOne(fullTankId)
        if (fullTank) {
            if (fullTank.vehicle.id != vehicleId) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            return fullTank
        }
        return null
    }

    @Override
    Page<FullTank> getFullTanks(Pageable pageable, String vehicleId) throws TechnicalException {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
        return fullTankRepository.findByVehicleId(vehicleId, pageable)
    }

    @Override
    FullTank save(FullTank fullTank, String vehicleId) throws TechnicalException {
        if (fullTank) {
            def vehicle = vehicleRepository.findOne(vehicleId)
            if (!vehicle) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            return modifyAndSave(fullTank, vehicle)
        }
        throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT)
    }

    @Override
    FullTank update(FullTank fullTank, String vehicleId) throws TechnicalException {
        if (fullTank) {
            def vehicle = vehicleRepository.findOne(vehicleId)
            if (!vehicle) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            def oldFullTank = fullTankRepository.findOne(fullTank.id)
            if (!oldFullTank) {
                throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTank.id)
            }
            vehicle.kilometers -= oldFullTank.distance
            vehicleRepository.save(vehicle)
            return modifyAndSave(fullTank, vehicle)
        }
        throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT)
    }

    @Override
    void delete(String vehicleId, String fullTankId) throws TechnicalException {
        def fullTank = fullTankRepository.findOne(fullTankId)
        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
        }
        if (fullTank.vehicle.id != vehicleId) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_VEHICLE_NOT_MATCH, errorParameter: vehicleId)
        }
        fullTank.vehicle.kilometers -= fullTank.distance
        vehicleRepository.save(fullTank.vehicle)
        fullTankRepository.delete(fullTank)
    }

    @Override
    List<StatDTO> getCostStats(String vehicleId) {
        checkVehicle(vehicleId)

        def fullTanks = fullTankRepository.findAllByVehicleId(vehicleId)
        def stats = []
        def costAndDate = fullTanks.sort { it.date }.collect { [it.cost, it.date] }
        costAndDate.each {
            stats << new StatDTO(date: it[1].format('dd/MM/yyyy'), value: it[0])
        }

        return stats
    }

    @Override
    List<StatDTO> getDistanceStats(String vehicleId) {
        checkVehicle(vehicleId)

        def fullTanks = fullTankRepository.findAllByVehicleId(vehicleId)
        def stats = []
        def distanceAndDate = fullTanks.sort { it.date }.collect { [it.distance, it.date] }
        distanceAndDate.each {
            stats << new StatDTO(date: it[1].format('dd/MM/yyyy'), value: it[0])
        }

        return stats
    }

    List<StatAverageDTO> getAverageStats(String vehicleId) {
        checkVehicle(vehicleId)
        def fullTanks = fullTankRepository.findAllByVehicleIdOrderByDateAsc(vehicleId)

        BigDecimal cost = 0
        BigDecimal dist = 0
        BigDecimal numberOfDay = 0
        BigDecimal quantity = 0

        Date previousDate
        use(TimeCategory) {
            fullTanks.each {
                cost += it.cost
                dist += it.distance
                quantity += it.quantity
                if (previousDate) {
                    def difference = it.date - previousDate
                    numberOfDay += difference.days
                }
                previousDate = it.date
            }
        }

        cost /= fullTanks.size() ?: 1
        dist /= fullTanks.size() ?: 1
        numberOfDay /= (fullTanks.size() - 1) ?: 1
        quantity /= fullTanks.size() ?: 1

        cost.setScale(2, RoundingMode.UP)
        dist.setScale(2, RoundingMode.UP)
        numberOfDay.setScale(2, RoundingMode.UP)
        quantity.setScale(2, RoundingMode.UP)

        def avgCost = new StatAverageDTO(label: "Cost avg", value: cost)
        def avgDist = new StatAverageDTO(label: "Dist avg", value: dist)
        def avgDay = new StatAverageDTO(label: "Day avg", value: numberOfDay)
        def avgQuantity = new StatAverageDTO(label: "Quantity avg", value: quantity)

        return [avgCost, avgDist, avgDay, avgQuantity]
    }

    private void checkVehicle(String vehicleId) {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
    }

    private FullTank modifyAndSave(FullTank fullTank, Vehicle vehicle) {
        vehicle.kilometers += fullTank.distance
        fullTank.vehicle = vehicleRepository.save(vehicle)
        return fullTankRepository.save(fullTank)
    }
}
